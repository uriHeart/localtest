package com.argo.common.domain.vendor;

import com.argo.common.configuration.ArgoBizException;
import com.argo.common.domain.address.RefinedAddressDto;
import com.argo.common.domain.address.kakao.KakaoAddressRefiner;
import com.argo.common.domain.order.RefinedAddress;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class VendorWorkplaceService {

    @Autowired
    VendorWorkplaceRepository vendorWorkplaceRepository;

    @Autowired
    VendorService vendorService;

    @Autowired
    KakaoAddressRefiner kakaoAddressRefiner;

    @Autowired
    workplaceTypeFilter workplaceTypeFilter;


    public ResponseEntity<VendorWorkplaceReturnParam> getListPerVendor(Long vendorId) {
        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorAndDeletedIsFalseOrderByCreatedAtDesc(vendorService.getVendor(vendorId));

        List<VendorWorkplaceShorten> returnList = new ArrayList<>();
        for (VendorWorkplace workplace : ListOfWorkplaces) {
            VendorWorkplaceShorten shorten = new VendorWorkplaceShorten();
            log.info(" receive type : {}", workplaceTypeFilter.toKorean(workplace.getType(), workplace.getEtcDetail()));
            shorten.setWorkplaceId(workplace.getVendorWorkplaceId());
            shorten.setType(workplaceTypeFilter.toKorean(workplace.getType(), workplace.getEtcDetail()));
            if (workplace.getWorkplaceName().equals("")) {
                shorten.setWorkplaceName("N/A");
            } else {
                shorten.setWorkplaceName(workplace.getWorkplaceName());
            }
            shorten.setAddress(workplace.getFullAddress());
            shorten.setLatitude(workplace.getLatitude());
            shorten.setLongitude(workplace.getLongitude());
            shorten.setCreatedAt(workplace.getCreatedAt());
            returnList.add(shorten);
        }
        return new ResponseEntity<>(VendorWorkplaceReturnParam.builder()
                .success(true)
                .vendorId(vendorId)
                .rowData(returnList)
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<VendorWorkplaceReturnParam> fullMap(Long vendorId) {

        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorAndDeletedIsFalseOrderByCreatedAtDesc(vendorService.getVendor(vendorId));
        List<VendorWorkplaceMapData> mapDataList = new ArrayList<>();
        for (VendorWorkplace workplace : ListOfWorkplaces) {
            VendorWorkplaceMapData mapData = new VendorWorkplaceMapData();
            mapData.setVendorWorkplaceId(workplace.getVendorWorkplaceId());
            mapData.setWorkplaceName(workplace.getWorkplaceName());
            mapData.setFullAddress(workplace.getFullAddress());
            mapData.setLatitude(workplace.getLatitude());
            mapData.setLongitude(workplace.getLongitude());
            mapDataList.add(mapData);
        }

        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .success(true)
                .vendorId(vendorId)
                .mapData(mapDataList)
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<VendorWorkplaceReturnParam> addWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        log.info(" receiver : {}", receiveParam);
        /* 빌더 vs Object.set */
//        VendorWorkplace workplace =
//                VendorWorkplace.builder()
//                        .vendor(vendorService.getVendor(receiveParam.getVendorId()))
//                        .fullAddress(receiveParam.getFullAddress())
//                        .type(receiveParam.getType())
//                        .jibunAddress(receiveParam.getJibunAddress())
//                        .jibunAddressEnglish(receiveParam.getJibunAddressEnglish())
//                        .roadAddress(receiveParam.getRoadAddress())
//                        .roadAddressEnglish(receiveParam.getRoadAddressEnglish())
//                        .postCode(receiveParam.getPostCode())
//                        .zipCode(receiveParam.getZipCode())
//                        .nationlInfo(receiveParam.getNationalInfo())
//                        .createdAt(new Date())
//                        .build();
//        vendorWorkplaceRepository.saveAndFlush(workplace);
//        VendorWorkplace workplace = receiveParamToWorkPlace(receiveParam);
//

        try {
            RefinedAddressDto refinedAddress = kakaoAddressRefiner.refine(receiveParam.getJibunAddress()).getRefinedAddress();
            Double latitude = refinedAddress.getLatitude();
            Double longitude = refinedAddress.getLongitude();
            VendorWorkplace newWorkplace = receiveParamToWorkPlace(receiveParam);
            newWorkplace.setLatitude(latitude);
            newWorkplace.setLongitude(longitude);
            vendorWorkplaceRepository.saveAndFlush(newWorkplace);
            return new ResponseEntity<>(VendorWorkplaceReturnParam
                    .builder()
                    .vendorId(newWorkplace.getVendor().getVendorId())
                    .workplaceId(newWorkplace.getVendorWorkplaceId())
                    .success(true)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build(), HttpStatus.OK);

        } catch (ArgoBizException e) {
            return new ResponseEntity<>(VendorWorkplaceReturnParam.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public VendorWorkplace receiveParamToWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        /* N/A 로 넣을지? */
//        if (receiveParam.getWorkplaceName().equals("")) {
//            newWorkplace.setWorkplaceName("N/A");
//        } else {
//            newWorkplace.setWorkplaceName(receiveParam.getWorkplaceName());
//        }
        VendorWorkplace newWorkplace = new VendorWorkplace();
        newWorkplace.setVendor(vendorService.getVendor(receiveParam.getVendorId()));
        log.info(" receive type : {}", receiveParam.getTypeNum());
        log.info(" refined type : {}", workplaceTypeFilter.receiverFilter(receiveParam.getTypeNum()));
        newWorkplace.setType(workplaceTypeFilter.receiverFilter(receiveParam.getTypeNum()));
        newWorkplace.setEtcDetail(receiveParam.getEtcDetail());
        newWorkplace.setWorkplaceName(receiveParam.getWorkplaceName());
        newWorkplace.setZipCode(receiveParam.getZipCode());
        newWorkplace.setPostCode(receiveParam.getPostCode());
        newWorkplace.setFullAddress(receiveParam.getFullAddress());
        newWorkplace.setJibunAddress(receiveParam.getJibunAddress());
        newWorkplace.setJibunAddressEnglish(receiveParam.getJibunAddressEnglish());
        newWorkplace.setRoadAddress(receiveParam.getRoadAddress());
        newWorkplace.setRoadAddressEnglish(receiveParam.getRoadAddressEnglish());
        newWorkplace.setNationlInfo(receiveParam.getNationalInfo());
        return newWorkplace;
    }
}