package com.argo.collect.domain.auth;

import com.argo.collect.domain.enums.SalesChannel;
import com.argo.common.domain.vendor.VendorChannel;

public interface AuthorityManager {
    public boolean isTargetChannel(SalesChannel channel);
    public String requestAuth(VendorChannel channel);
}
