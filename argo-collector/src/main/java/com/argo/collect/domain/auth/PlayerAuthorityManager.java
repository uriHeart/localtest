package com.argo.collect.domain.auth;

import com.argo.collect.domain.enums.SalesChannel;
import com.argo.collect.domain.util.ArgoScriptEngineManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerAuthorityManager implements AuthorityManager {
    @Autowired
    private ArgoScriptEngineManager scriptEngineManager;

    @Override
    public boolean isTargetChannel(SalesChannel channel) {
        return SalesChannel.PLAYER == channel;
    }

    @Override
    public String requestAuth(AuthorityParam param) {
        String loginUrl = param.getBaseUrl() + "/po/login/make_login";
        String token = this.getToken(param);
        String encodePassword = this.getEncodePassword(token, param.getPassword());

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

            if (result == null || !"1".equals(result.get("code").toString())) {
                return null;
            }

            return con.getHeaderFields().get("Set-Cookie").stream().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // auth data cas insert

        return null;
    }

    private String getToken(AuthorityParam param) {
        String tokenUrl = param.getBaseUrl() + "/po/login/set_token";
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
            return scriptEngineManager.getScriptEngine().eval("var temp = cryptDes.des(" + token + "," + password + ", 1, 0); cryptDes.stringToHex(temp);").toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
