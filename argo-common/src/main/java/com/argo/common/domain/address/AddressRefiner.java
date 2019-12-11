package com.argo.common.domain.address;

public interface AddressRefiner {
    public RefineResultDto addressRefine(String address);
    public boolean isTargetService();
}
