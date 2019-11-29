package com.argo.collect.domain.auth;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.util.ArgoScriptEngineManager;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class Cafe24AuthorityManagerTest extends AbstractAuthorityManager {

    @Autowired
    private ArgoScriptEngineManager scriptEngineManager;

    @Override
    public boolean isTargetChannel(String channel) {
        return false;
    }

    @Override
    public String requestAuth(VendorChannel channel) {
        AuthorityParam param = super.getParam(channel);
        String loginUrl = param.getBaseUrl() + param.getLoginUrl();
        String token ="";// this.getToken(param);
        String encodePassword ="";// this.getEncodePassword(token, param.getPassword());

        String urlParameters  = "id=" + param.getId() + "&password=" + param.getPassword() + "&pw=" + encodePassword + "&error=0";
        try {
            URL obj = new URL(loginUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(HttpMethod.POST.name());
            con.setRequestProperty("Cookie", "token=" + token + ";");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

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

            if (result == null || !"1".equals(result.get("cd").toString())) {
                return null;
            }

            return con.getHeaderFields().get("Set-Cookie").stream().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // auth data cas insert

        return null;
    }


    @Test
    public void run() {

    }
}

