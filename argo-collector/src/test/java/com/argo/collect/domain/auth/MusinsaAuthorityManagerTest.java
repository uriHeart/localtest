package com.argo.collect.domain.auth;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.util.ArgoScriptEngineManager;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(
//        properties = {
//                "value=test"
//        },
//        classes = {ArgoCollectorApplication.class},
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//)
public class MusinsaAuthorityManagerTest extends AbstractAuthorityManager {

    @Autowired
    private ArgoScriptEngineManager scriptEngineManager;


    @Override
    public boolean isTargetChannel(String channel) {
        return false;
    }
    @Autowired
    private VendorService vendorService;

    @Override
    public String requestAuth(VendorChannel channel) {

        AuthorityParam param = super.getParam(channel);
        String loginUrl = param.getBaseUrl() + param.getLoginUrl();
//        String loginUrl = "https://bizest.musinsa.com/po/api/login";
        String token = this.getToken(param);
        String encodePassword = this.getEncodePassword(token, param.getPassword());

        String urlParameters  = "type=&url=&id=" + param.getId() + "&passwd=" + param.getPassword() + "&pw=" + encodePassword + "&error=0";


        try {
            URL obj = new URL(loginUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(HttpMethod.POST.name());
            con.setRequestProperty("cookie", "token=" + token + ";");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            //type=&url=&pw=0x3830f69e58ec82611e36223c9504c918&error=0&id=goal&passwd=wagti2019!@
            Map result = null;
            try (InputStream in = con.getInputStream();
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                byte[] buf = new byte[1024 * 8];
                int length = 0;
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(new String(out.toByteArray(), "UTF-8"), Map.class);
            }

            //1성공 값 리턴
            // -5 실패
            if (result == null || !"1".equals(result.get("cd").toString())) {
                return null;
            }

            con.getHeaderFields().get("Set-Cookie").forEach(s->{
                System.out.println(s);
            });

            return con.getHeaderFields().get("Set-Cookie").stream().collect(Collectors.joining()).replace("domain=bizest.musinsa.com","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getToken(AuthorityParam param) {
        String tokenUrl = param.getBaseUrl() + param.getTokenUrl();
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
        return result != null ? result.get("token").toString() : null;
    }

    private String getEncodePassword(String token, String password) {
        try {
            return scriptEngineManager.getScriptEngine().eval("var temp = cryptDes.des('" + token + "','" + password + "', 1, 0); cryptDes.stringToHex(temp);").toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void run(){
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==8){
                requestAuth(channel);
            }
        }
    }
}
