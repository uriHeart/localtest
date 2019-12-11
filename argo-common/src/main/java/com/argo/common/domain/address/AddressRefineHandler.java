package com.argo.common.domain.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressRefineHandler {
    @Autowired
    private List<AddressRefiner> refiners;

    public RefineResultDto refine(String address) {
        for (AddressRefiner r : refiners) {
            if (!r.isTargetService()) {
                continue;
            }
            return r.addressRefine(address);
        }
        return null;
    }
}
