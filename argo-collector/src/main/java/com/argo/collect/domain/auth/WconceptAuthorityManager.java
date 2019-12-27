package com.argo.collect.domain.auth;

import com.argo.common.domain.vendor.VendorChannel;
import org.springframework.stereotype.Component;

@Component
public class WconceptAuthorityManager extends AbstractAuthorityManager {

    @Override
    public boolean isTargetChannel(String channel) {
        return "W_CONCEPT".equals(channel);
    }

    @Override
    public String requestAuth(VendorChannel channel) {
        AuthorityParam param = super.getParam(channel);
        String loginInfo = param.getId() +":"+param.getPassword();
        return loginInfo;
    }
}
