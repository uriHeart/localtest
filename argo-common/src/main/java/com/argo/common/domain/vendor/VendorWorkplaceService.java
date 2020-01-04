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
            shorten.setWorkplaceName(workplace.getWorkplaceName());
            /* if user_address == null ,then admin 코드필요 */
            shorten.setAddress(workplace.getFullAddress());
            shorten.setLatitude(workplace.getAdminSelectedLatitude());
            shorten.setLongitude(workplace.getAdminSelectedLongitude());
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
        log.info("vendor ID is : {} ", vendorId);
        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorAndDeletedIsFalseOrderByCreatedAtDesc(vendorService.getVendor(vendorId));
        List<VendorWorkplaceMapData> mapDataList = new ArrayList<>();
        for (VendorWorkplace workplace : ListOfWorkplaces) {
            VendorWorkplaceMapData mapData = new VendorWorkplaceMapData();
            mapData.setVendorWorkplaceId(workplace.getVendorWorkplaceId());
            mapData.setWorkplaceName(workplace.getWorkplaceName());
            mapData.setFullAddress(workplace.getFullAddress());
            //STREAM 으로 바꾸고 latitude longitude (user 꺼 priority)
            mapData.setLatitude(workplace.getAdminSelectedLatitude());
            mapData.setLongitude(workplace.getAdminSelectedLongitude());
            mapDataList.add(mapData);
        }

        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .success(true)
                .vendorId(vendorId)
                .mapData(mapDataList)
                .build(), HttpStatus.OK);
    }

    private Boolean validate(Integer hashCode) {
        return vendorWorkplaceRepository.existsVendorWorkplaceByHashCodeEqualsAndDeletedIsFalse(hashCode);
    }
    public ResponseEntity<VendorWorkplaceReturnParam> addWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        log.info(" receiver : {}", receiveParam);
        Integer targetHashCode = receiveParam.getTypeNum().toString().hashCode() + receiveParam.getFullAddress().hashCode() + receiveParam.getWorkplaceName().hashCode();
        log.info(" 타입 해쉬 : {}", receiveParam.getTypeNum().toString().hashCode());
        log.info(" 주소 해쉬 : {}", receiveParam.getFullAddress().hashCode());
        log.info(" 이름 해쉬 : {}", receiveParam.getWorkplaceName().hashCode());
        log.info(" receiver : {}", targetHashCode);
        if (validate(targetHashCode)) {
            return new ResponseEntity<>(VendorWorkplaceReturnParam.builder()
                    .success(false)
                    .message("이미 등록된 작업장입니다")
                    .build(), HttpStatus.OK);
        }
        try {
            VendorWorkplace newWorkplace = receiveParamToWorkPlace(receiveParam);
            newWorkplace.setHashCode(targetHashCode);
            vendorWorkplaceRepository.saveAndFlush(newWorkplace);
            return new ResponseEntity<>(VendorWorkplaceReturnParam
                    .builder()
                    .vendorId(newWorkplace.getVendor().getVendorId())
                    .workplaceId(newWorkplace.getVendorWorkplaceId())
                    .success(true)
                    .build(), HttpStatus.OK);
        } catch (ArgoBizException e) {
            return new ResponseEntity<>(VendorWorkplaceReturnParam.builder().success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private VendorWorkplace receiveParamToWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        /* N/A 로 넣을지? */
//        if (receiveParam.getWorkplaceName().equals("")) {
//            newWorkplace.setWorkplaceName("N/A");
//        } else {
//            newWorkplace.setWorkplaceName(receiveParam.getWorkplaceName());
//        }
        log.info(" receiver is : {}", receiveParam);
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
        newWorkplace.setAdminSelectedLatitude(receiveParam.getAdminSelectedLatitude());
        newWorkplace.setAdminSelectedLongitude(receiveParam.getAdminSelectedLongitude());
        newWorkplace.setUserSelectedLatitude(receiveParam.getUserSelectedLatitude());
        newWorkplace.setUserSelectedLongitude(receiveParam.getUserSelectedLongitude());
        newWorkplace.setRoadAddress(receiveParam.getRoadAddress());
        newWorkplace.setRoadAddressEnglish(receiveParam.getRoadAddressEnglish());
        newWorkplace.setNationlInfo(receiveParam.getNationalInfo());
        return newWorkplace;
    }

    public ResponseEntity<VendorWorkplaceReturnParam> getLocation(String address) {
        try {
            RefinedAddressDto refinedAddress = kakaoAddressRefiner.refine(address).getRefinedAddress();
            Double latitude = refinedAddress.getLatitude();
            Double longitude = refinedAddress.getLongitude();
            log.info("admin_latitude {}", latitude);
            log.info("admin_longitude {}", longitude);
            return new ResponseEntity<>(VendorWorkplaceReturnParam
                    .builder()
                    .success(true)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build(), HttpStatus.OK);

        } catch (ArgoBizException e) {
            return new ResponseEntity<>(VendorWorkplaceReturnParam.builder().success(false).message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<VendorWorkplaceReturnParam> viewEachMap(Long workplaceId) {
        VendorWorkplace target = vendorWorkplaceRepository.findByVendorWorkplaceId(workplaceId);
        Double latitude = target.getUserSelectedLatitude();
        Double longitude = target.getUserSelectedLongitude();
        // 사용자 지정 좌표가 없을시 admin 꺼로 대체
        if (latitude == null || longitude == null) {
            latitude = target.getAdminSelectedLatitude();
            longitude = target.getAdminSelectedLongitude();
        }
        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .workplaceId(workplaceId)
                .latitude(latitude)
                .longitude(longitude)
                .build(), HttpStatus.OK
        );
    }
}