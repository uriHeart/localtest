package com.argo.collect.domain.auth;

import com.argo.collect.domain.util.ArgoScriptEngineManager;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.vendor.VendorChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EZAdminAuthorityManager extends AbstractAuthorityManager {
    @Autowired
    private ArgoScriptEngineManager scriptEngineManager;

    @Override
    public boolean isTargetChannel(String channel) {
        return "EZ_ADMIN".equals(channel);
    }

    @Override
    public String requestAuth(VendorChannel channel) {
        AuthorityParam param = super.getParam(channel);
        String loginUrl = param.getBaseUrl() + param.getLoginUrl();
        String encodeValue = this.getEncodeValue(param);
        String urlParameters = "encpar=" + encodeValue;
        try {
            URL obj = new URL(loginUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod(HttpMethod.POST.name());
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            super.getResult(con.getInputStream(), true);

            return con.getHeaderFields().get("Set-Cookie").stream().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getEncodeValue(AuthorityParam param) {
        String queryString = "ezsaved=&identifier=c6bf20e05379959d489e8451f7f33c0a&crdate=" + ArgoDateUtil.getDateString(LocalDate.now())
                + "&domain=wagti&userid=" + param.getId() + "&passwd=" + param.getPassword();
        try {
            String script = "var rsa = new RSAKey();" +
                    "rsa.setPublic('80863e5e41076dbff1e46891a0eed30bff4a87528e6841088245585455d5bbcfaa2f16e7f8a46f0e3624deeab2d2e9fbf0f981feb77749a739542712db60708f6f870282259f5fa6d2252e6c00cbc36d95cf94710a0d456641edfd60cfd53e5d6a3ebc5ef943ce8aed0b5f39dc58bba0da677f5dfc97950dded75334714661c5', '010001');" +
                    "rsa.encrypt('" + queryString + "');";
            return scriptEngineManager.getScriptEngine().eval(script).toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
