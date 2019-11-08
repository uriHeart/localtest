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
public class TwentyNineAuthorityManagerTest  {
    public static void main(String[] args) throws Exception {
        String loginUrl = "https://apiportal.29cm.co.kr/admin/login/";
        String dataJson =  "{\"admin_id\": \"goalstudio\", \"password\": \"Wagti2019!@\"}";
        URL obj = new URL(loginUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(HttpMethod.POST.name());
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);              //항상 갱신된내용을 가져옴.
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(dataJson);
        wr.flush();
        wr.close();

        for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
            for (String value : header.getValue()) {
                System.out.println(header.getKey() + " : " + value);
            }
        }

        String cookie = con.getHeaderFields().get("set-cookie").stream().collect(Collectors.joining());
        System.out.println(cookie);
//        String authorization = con.getHeaderFields().get("authorization").stream().collect(Collectors.joining());
//        System.out.println(authorization);

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

        RestTemplate restTemplate = new RestTemplate();
        String dataUrl = "https://apiportal.29cm.co.kr/order/orders/cs-list/?start_date=2019-11-07%2000:00:00&end_date=2019-11-08%2023:59:59&limit=10&offset=0";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("authorization", "Bearer ciOdGWnTtVb10jSH2aOoFbl7DsknkhYyD60ztHzwLTP2FpNVopJYkBtg1T8s");
        headers.add("cookie", cookie);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//        map.add("start_date", "2019-10-03");
//        map.add("end_date", "2019-11-03");
//        map.add("limit", "10");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(dataUrl, HttpMethod.GET, request, String.class);
        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();
//            Map data = objectMapper.readValue(dataResult, Map.class);
//            log.info("data : {}", dataResult);
        }
    }
}
