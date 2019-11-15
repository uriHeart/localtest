package com.argo.api.controller.vendor;

import com.argo.api.auth.AuthUser;
import com.argo.api.auth.UserManager;
import com.argo.common.configuration.ArgoBizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VendorValidator {
//    @Autowired
    private UserManager userManager;

    public void valid(Long vendorId) {
        AuthUser user = userManager.get();
        if (vendorId == null || !vendorId.equals(user.getVendorId())) {
            throw new ArgoBizException("올바르지 않은 벤더 정보입니다.");
        }
    }
}
