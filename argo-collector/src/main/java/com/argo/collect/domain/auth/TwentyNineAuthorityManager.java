package com.argo.collect.domain.auth;

import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TwentyNineAuthorityManager extends AbstractAuthorityManager {
    @Override
    public boolean isTargetChannel(String channel) {
        return "TWENTY_NINE_CM".equals(channel);
    }

    @Override
    public String requestAuth(VendorChannel channel) {
        AuthorityParam param = super.getParam(channel);
        String loginUrl = param.getBaseUrl() + param.getLoginUrl();
        String dataJson =  "{admin_id: \"" + param.getId() + "\", password: \"" + param.getPassword() + "\"}";
        try {
            URL obj = new URL(loginUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(HttpMethod.POST.name());
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(dataJson);
            wr.flush();
            wr.close();

            Map result = super.getResult(con.getInputStream(), false);

            if (result == null || !"partner".equals(result.get("message").toString())) {
                return null;
            }

            return con.getHeaderFields().get("set-Cookie").stream().collect(Collectors.joining(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
