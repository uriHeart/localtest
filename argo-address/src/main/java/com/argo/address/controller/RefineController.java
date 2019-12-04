package com.argo.address.controller;

import com.argo.common.domain.address.AddressRefiner;
import com.argo.common.domain.address.RefineResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RefineController {
    @Autowired
    private List<AddressRefiner> refiners;

    @GetMapping("/refine-address")
    public RefineResultDto refine(@RequestParam String address) {
        for (AddressRefiner r : refiners) {
            if (!r.isTargetService()) {
                continue;
            }
            return r.addressRefine(address);
        }
        return null;
    }
}
