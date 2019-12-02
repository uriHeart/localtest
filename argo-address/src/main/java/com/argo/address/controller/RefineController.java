package com.argo.address.controller;

import com.argo.address.service.AddressRefiner;
import com.argo.common.dto.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RefineController {
    @Autowired
    private AddressRefiner refiner;

    @GetMapping("/refine-address")
    public SearchResult refine(@RequestParam String address) {
        return refiner.refine(address);
    }
}
