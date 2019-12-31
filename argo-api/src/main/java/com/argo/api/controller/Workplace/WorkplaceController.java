package com.argo.api.controller.Workplace;


import com.argo.api.service.gopotal.Response;
import com.argo.common.domain.address.kakao.KakaoAddressRefiner;
import com.argo.common.domain.vendor.*;
import lombok.*;
import org.apache.tools.ant.taskdefs.condition.Http;
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
        return vendorWorkplaceService.getListPerVendor(vendorId);
    }


    @GetMapping(value = "/map/{workplaceId}")
    public ResponseEntity<VendorWorkplaceReturnParam> viewMap(@PathVariable Long workplaceId) {
        VendorWorkplace target = vendorWorkplaceRepository.findByVendorWorkplaceId(workplaceId);
        Double latitude = target.getLatitude();
        Double longitude = target.getLongitude();

        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .workplaceId(workplaceId)
                .latitude(latitude)
                .longitude(longitude)
                .build(), HttpStatus.OK
        );
    }

    @GetMapping(value = "/fullMap/{vendorId}")
    public ResponseEntity<VendorWorkplaceReturnParam> fullMap(@PathVariable Long vendorId) {
        return vendorWorkplaceService.fullMap(vendorId);
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
        return vendorWorkplaceService.addWorkPlace(receiveParam);
    }

    @PutMapping(value="/addworkplace/location")
    public ResponseEntity<VendorWorkplaceReturnParam> updateLocation(@RequestParam Long workplaceId, Double latitude, Double longitude) {
        VendorWorkplace target = vendorWorkplaceRepository.findByVendorWorkplaceId(workplaceId);
        target.setLatitude(latitude);
        target.setLongitude(longitude);
        vendorWorkplaceRepository.saveAndFlush(target);
        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .success(true)
                .workplaceId(target.getVendorWorkplaceId())
                .build(), HttpStatus.OK);
    }
}
