package com.argo.api.controller.Workplace;


import com.argo.api.service.gopotal.Response;
import com.argo.common.domain.address.kakao.KakaoAddressRefiner;
import com.argo.common.domain.vendor.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    @Autowired
    KakaoAddressRefiner kakaoAddressRefiner;

    @GetMapping(value = "/list/{vendorId}")
    public ResponseEntity<VendorWorkplaceReturnParam> findWorkPlaces(@PathVariable Long vendorId) {
        System.out.println("error");
        return vendorWorkplaceService.getListPerVendor(vendorId);
    }

    @GetMapping(value = "/delete/{workplaceId}")
    public ResponseEntity<VendorWorkplaceReturnParam> findWorkplaces(@PathVariable Long workplaceId) {
        VendorWorkplace target = vendorWorkplaceRepository.findByVendorWorkplaceId(workplaceId);
        target.setDeleted(true);
        vendorWorkplaceRepository.saveAndFlush(target);
        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .success(true)
                .build(), HttpStatus.OK);
    }

    @PostMapping(value= "/addworkplace")
    public ResponseEntity<VendorWorkplaceReturnParam> addWorkPlace(@RequestBody VendorWorkplaceReceiveParam receiveParam) throws IOException {
        System.out.println(receiveParam);
        return vendorWorkplaceService.addWorkPlace(receiveParam);
    }
}
