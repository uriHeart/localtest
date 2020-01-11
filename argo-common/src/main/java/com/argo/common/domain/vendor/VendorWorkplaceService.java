package com.argo.common.domain.vendor;

import com.argo.common.configuration.ArgoBizException;
import com.argo.common.domain.address.RefinedAddressDto;
import com.argo.common.domain.address.kakao.KakaoAddressRefiner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    WorkplaceTypeFilter WorkplaceTypeFilter;


    //stream 불가.
    public ResponseEntity<VendorWorkplaceResultDto> getListPerVendor(Long vendorId) {
        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorAndDeletedIsFalseOrderByCreatedAtDesc(vendorService.getVendor(vendorId));

        List<VendorWorkplaceShorten> returnList = new ArrayList<>();
        for (VendorWorkplace workplace : ListOfWorkplaces) {
            VendorWorkplaceShorten shorten = new VendorWorkplaceShorten();
            log.info(" receive type : {}", WorkplaceTypeFilter.toKorean(workplace.getType(), workplace.getEtcDetail()));
            shorten.setWorkplaceId(workplace.getVendorWorkplaceId());
            shorten.setType(WorkplaceTypeFilter.toKorean(workplace.getType(), workplace.getEtcDetail()));
            shorten.setWorkplaceName(workplace.getWorkplaceName());
            shorten.setAddress(workplace.getFullAddress());
            Double latitude = workplace.getUserSelectedLatitude();
            Double longitude = workplace.getUserSelectedLongitude();
            if (latitude == null || longitude == null) {
                latitude = workplace.getAdminSelectedLatitude();
                longitude = workplace.getAdminSelectedLongitude();
            }
            shorten.setLatitude(latitude);
            shorten.setLongitude(longitude);
            shorten.setCreatedAt(workplace.getCreatedAt());
            returnList.add(shorten);
        }
        return new ResponseEntity<>(VendorWorkplaceResultDto.builder()
                .success(true)
                .vendorId(vendorId)
                .rowData(returnList)
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<VendorWorkplaceResultDto> fullMap(Long vendorId) {
        log.info("vendor ID is : {} ", vendorId);
        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorAndDeletedIsFalseOrderByCreatedAtDesc(vendorService.getVendor(vendorId));
        List<VendorWorkplaceMapData> mapDataList = ListOfWorkplaces.stream().filter(w -> !w.isDeleted()).map(VendorWorkplaceMapData::from).collect(Collectors.toList());
        log.info("mapdata: {} ", mapDataList);
        return new ResponseEntity<>(VendorWorkplaceResultDto
                .builder()
                .success(true)
                .vendorId(vendorId)
                .mapData(mapDataList)
                .build(), HttpStatus.OK);
    }

    private Boolean validate(Integer hashCode) {
        return vendorWorkplaceRepository.existsVendorWorkplaceByHashCodeEqualsAndDeletedIsFalse(hashCode);
    }

    @Transactional(readOnly = false)
    public ResponseEntity<VendorWorkplaceResultDto> addWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        Integer targetHashCode = receiveParam.getTypeNum().toString().hashCode() + receiveParam.getFullAddress().hashCode() + receiveParam.getWorkplaceName().hashCode();
        log.info("hashCode : {}", targetHashCode);
        if (validate(targetHashCode)) {
            return new ResponseEntity<>(VendorWorkplaceResultDto.builder()
                    .success(false)
                    .message("이미 등록된 작업장입니다")
                    .build(), HttpStatus.OK);
        }
        try {
            VendorWorkplace newWorkplace = receiveParamToWorkPlace(receiveParam);
            newWorkplace.setHashCode(targetHashCode);
            vendorWorkplaceRepository.save(newWorkplace);
            return new ResponseEntity<>(VendorWorkplaceResultDto
                    .builder()
                    .vendorId(newWorkplace.getVendor().getVendorId())
                    .workplaceId(newWorkplace.getVendorWorkplaceId())
                    .success(true)
                    .build(), HttpStatus.OK);
        } catch (ArgoBizException e) {
            return new ResponseEntity<>(VendorWorkplaceResultDto.builder().success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private VendorWorkplace receiveParamToWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        VendorWorkplace newWorkplace = new VendorWorkplace();
        newWorkplace.setVendor(vendorService.getVendor(receiveParam.getVendorId()));
        log.info(" refined type : {}", WorkplaceTypeFilter.receiverFilter(receiveParam.getTypeNum()));
        newWorkplace.setType(WorkplaceTypeFilter.receiverFilter(receiveParam.getTypeNum()));
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

    public ResponseEntity<VendorWorkplaceResultDto> getLocation(String address) {
        try {
            RefinedAddressDto refinedAddress = kakaoAddressRefiner.refine(address).getRefinedAddress();
            Double latitude = refinedAddress.getLatitude();
            Double longitude = refinedAddress.getLongitude();
            log.info("admin_latitude {}", latitude);
            log.info("admin_longitude {}", longitude);
            return new ResponseEntity<>(VendorWorkplaceResultDto
                    .builder()
                    .success(true)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build(), HttpStatus.OK);

        } catch (ArgoBizException e) {
            return new ResponseEntity<>(VendorWorkplaceResultDto.builder().success(false).message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<VendorWorkplaceResultDto> viewEachMap(Long workplaceId) {
        VendorWorkplace target = vendorWorkplaceRepository.findByVendorWorkplaceId(workplaceId);
        Double latitude = target.getUserSelectedLatitude();
        Double longitude = target.getUserSelectedLongitude();
        // 사용자 지정 좌표가 없을시 admin 꺼로 대체
        if (latitude == null || longitude == null) {
            latitude = target.getAdminSelectedLatitude();
            longitude = target.getAdminSelectedLongitude();
        }
        return new ResponseEntity<>(VendorWorkplaceResultDto
                .builder()
                .workplaceId(workplaceId)
                .latitude(latitude)
                .longitude(longitude)
                .build(), HttpStatus.OK
        );
    }

    @Transactional(readOnly = false)
    public ResponseEntity<VendorWorkplaceResultDto> delete(Long workplaceId) {
        VendorWorkplace target = vendorWorkplaceRepository.findByVendorWorkplaceId(workplaceId);
        target.setDeleted(true);
        vendorWorkplaceRepository.save(target);
        return new ResponseEntity<>(VendorWorkplaceResultDto
                .builder()
                .success(true)
                .build(), HttpStatus.OK);
    }
}