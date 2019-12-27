package com.argo.api.controller.Workplace;


import com.argo.common.domain.vendor.VendorWorkplaceReceiveParam;
import com.argo.common.domain.vendor.VendorWorkplaceService;
import com.argo.common.domain.vendor.VendorWorkplaceRepository;
import com.argo.common.domain.vendor.VendorWorkplaceReturnParam;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/workplace")
public class WorkplaceController {
    @Autowired
    VendorWorkplaceRepository vendorWorkplaceRepository;
    @Autowired
    VendorWorkplaceService vendorWorkplaceService;

    @GetMapping(value = "/list/{vendorId}")
    public ResponseEntity<VendorWorkplaceReturnParam> findWorkPlaces(@PathVariable Long vendorId) {
        System.out.println("error");
        return vendorWorkplaceService.getListPerVendor(vendorId);
    }

    @PostMapping(value= "/addworkplace")
    public ResponseEntity<VendorWorkplaceReturnParam> addWorkPlace(@RequestBody VendorWorkplaceReceiveParam receiveParam) throws IOException {
        System.out.println(receiveParam);
        return vendorWorkplaceService.addWorkPlace(receiveParam);
    }
}
