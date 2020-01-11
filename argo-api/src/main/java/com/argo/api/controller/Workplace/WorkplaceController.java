package com.argo.api.controller.Workplace;


import com.argo.common.domain.address.kakao.KakaoAddressRefiner;
import com.argo.common.domain.vendor.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/workplace")
public class WorkplaceController {
    @Autowired
    private VendorWorkplaceRepository vendorWorkplaceRepository;

    @Autowired
    private VendorWorkplaceService vendorWorkplaceService;

    @Autowired
    private KakaoAddressRefiner kakaoAddressRefiner;

    @Autowired
    private WorkplaceTypeFilter workplaceTypeFilter;


    @GetMapping(value = "/list/{vendorId}")
    public ResponseEntity<VendorWorkplaceResultDto> findWorkPlaces(@PathVariable Long vendorId) {
        return vendorWorkplaceService.getListPerVendor(vendorId);
    }


    @GetMapping(value = "/map/{workplaceId}")
    public ResponseEntity<VendorWorkplaceResultDto> viewMap(@PathVariable Long workplaceId) {
        return vendorWorkplaceService.viewEachMap(workplaceId);
    }

    @GetMapping(value = "/fullMap/{vendorId}")
    public ResponseEntity<VendorWorkplaceResultDto> fullMap(@PathVariable Long vendorId) {
        return vendorWorkplaceService.fullMap(vendorId);
    }

    @GetMapping(value = "/delete/{workplaceId}")
    public ResponseEntity<VendorWorkplaceResultDto> delete(@PathVariable Long workplaceId) {
        return vendorWorkplaceService.delete(workplaceId);
    }

    @PostMapping(value= "/addworkplace")
    public ResponseEntity<VendorWorkplaceResultDto> addWorkPlace(@RequestBody VendorWorkplaceReceiveParam receiveParam) throws IOException {
        return vendorWorkplaceService.addWorkPlace(receiveParam);
    }

    @GetMapping(value = "/enum")
    public List<EnumElem> returnEnum() {
        return workplaceTypeFilter.listEnum();
    }

    //Get Mapping 으로는 request param을 받을 수 없는것인지.
    @PutMapping(value= "/location")
    public ResponseEntity<VendorWorkplaceResultDto> getLocation(@RequestParam String address) {
        System.out.println("get loc");
        return vendorWorkplaceService.getLocation(address);
    }
}
