package com.argo.api.controller.Workplace;


import com.argo.common.domain.vendor.VendorWorkplaceService;
import com.argo.common.domain.vendor.VendorWorkplaceRepository;
import com.argo.common.domain.vendor.VendorWorkplaceReturnParam;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping(value = "/api")
public class WorkplaceController {
    @Autowired
    VendorWorkplaceRepository vendorWorkplaceRepository;
    @Autowired
    VendorWorkplaceService vendorWorkPlaceService;

    @GetMapping(value = "/workplace/list/{vendorId}")
    public ResponseEntity<VendorWorkplaceReturnParam> findWorkPlaces(@PathVariable Long vendorId) {
        System.out.println("error");
        return vendorWorkPlaceService.getListPerVendor(vendorId);
    }
}
