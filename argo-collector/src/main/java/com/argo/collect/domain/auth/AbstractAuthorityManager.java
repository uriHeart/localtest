package com.argo.collect.domain.auth;

import com.argo.common.domain.common.util.AES256Util;
import com.argo.common.domain.vendor.ChannelVendorAccount;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

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
}
