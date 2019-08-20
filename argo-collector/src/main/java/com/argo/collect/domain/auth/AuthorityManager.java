package com.argo.collect.domain.auth;

import com.argo.collect.domain.enums.SalesChannel;

public interface AuthorityManager {
    public boolean isTargetChannel(SalesChannel channel);
    public String requestAuth(AuthorityParam param);
}
