package com.argo.collect.domain.auth;

import com.argo.common.domain.common.util.AES256Util;
import com.argo.common.domain.vendor.ChannelVendorAccount;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;

public abstract class AbstractAuthorityManager implements AuthorityManager {
    @Autowired
    private VendorService vendorService;

    public AuthorityParam getParam(VendorChannel channel) {
        ChannelVendorAccount account = vendorService.getChannelVendorAccount(channel.getSalesChannel(), channel.getVendor());
        try {
            return AuthorityParam.builder()
                    .id(AES256Util.get().decrypt(account.getCredentialId()))
                    .password(AES256Util.get().decrypt(account.getCredentialPassword()))
                    .baseUrl(account.getSalesChannel().getBaseUrl())
                    .tokenUrl(account.getSalesChannel().getTokenUrl())
                    .loginUrl(account.getSalesChannel().getLoginUrl())
                    .build();
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map getResult(InputStream inputStream, boolean returnSkip) throws IOException {
        try (InputStream in = inputStream;
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            ObjectMapper mapper = new ObjectMapper();
            String temp = new String(out.toByteArray(), "UTF-8");
            if (returnSkip) {
                return null;
            }
            return mapper.readValue(temp, Map.class);
        }
    }
}
