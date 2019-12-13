package com.argo.collect.domain.auth;

import com.argo.collect.domain.util.ArgoScriptEngineManager;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.script.ScriptException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MusinsaAuthorityManager extends AbstractAuthorityManager {

    @Autowired
    private ArgoScriptEngineManager scriptEngineManager;


    @Override
    public boolean isTargetChannel(String channel) {
        return "MUSINSA".equals(channel);
    }

    @Override
    public String requestAuth(VendorChannel channel) {
        AuthorityParam param = super.getParam(channel);
        String loginUrl = param.getBaseUrl() + param.getLoginUrl();
        String token = this.getToken(param);
        String encodePassword = this.getEncodePassword(token, param.getPassword());

        String urlParameters  = "type=&url=&id=" + param.getId() + "&passwd=" + param.getPassword() + "&pw=" + encodePassword + "&error=0";
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
            String cookie = con.getHeaderFields().get("Set-Cookie").stream().collect(Collectors.joining()).replace("domain=bizest.musinsa.com","");
            con.disconnect();

            return cookie;
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
}