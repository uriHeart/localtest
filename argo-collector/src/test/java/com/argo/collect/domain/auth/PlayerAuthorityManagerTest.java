package com.argo.collect.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PlayerAuthorityManagerTest {

    public static void main(String[] args) throws Exception {

        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("JavaScript");
        File js = new File("/Users/ags0688/git/argo/cryptDes.js");
        try {
            FileReader reader = new FileReader(js);
            se.eval(reader);
        } catch (FileNotFoundException | ScriptException e) {
            e.printStackTrace();
        }

        String tokenUrl = "http://biz.player.co.kr/po/login/set_token";
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tokenUrl);
        builder.queryParam("UID", Math.random());
        String uriBuilder = builder.build().encode().toUriString();
        Map result = null;
        try {
            String response = restTemplate.getForObject(new URI(uriBuilder), String.class);
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(response, Map.class);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        log.info("result : {}", result);

        if (result == null) {
            return;
        }

        String token = result.get("token").toString();
        String password = "test";
        String encodePassword = null;
        try {
            encodePassword = se.eval("var temp = cryptDes.des('" + token + "','" + password + "', 1, 0); cryptDes.stringToHex(temp);").toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        String loginUrl = "http://biz.player.co.kr/po/login/make_login";
        String urlParameters  = "id=test&password=" + password + "&pw=" + encodePassword + "&error=0";
        URL obj = new URL(loginUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(HttpMethod.POST.name());
        con.setRequestProperty("Cookie", "token=" + token + ";");


        con.setDoOutput(true);              //항상 갱신된내용을 가져옴.
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
            for (String value : header.getValue()) {
                System.out.println(header.getKey() + " : " + value);
            }
        }

        String cookie = con.getHeaderFields().get("Set-Cookie").stream().collect(Collectors.joining());
        System.out.println(cookie);

        try (InputStream in = con.getInputStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            System.out.println(new String(out.toByteArray(), "UTF-8"));
        }

        // 접속 해제
        con.disconnect();

        String dataUrl = "http://biz.player.co.kr/po/order/ord01/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", cookie);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("S_SDATE", "2019-07-18");
        map.add("S_EDATE", "2019-08-18");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        String dataResult = restTemplate.postForObject(dataUrl, request , String.class);
        if (dataResult != null) {
           ObjectMapper objectMapper = new ObjectMapper();
           Map data = objectMapper.readValue(dataResult, Map.class);
           log.info("data : {}", dataResult);
        }
    }
}
