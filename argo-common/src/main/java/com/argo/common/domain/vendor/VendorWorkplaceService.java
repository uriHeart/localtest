package com.argo.common.domain.vendor;

import com.argo.common.configuration.ArgoBizException;
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
    protected RestTemplate restTemplate;


    public ResponseEntity<VendorWorkplaceReturnParam> getListPerVendor(Long vendorId) {
        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorOrderByType(vendorService.getVendor(vendorId));

        String vendorName = vendorService.getVendor(vendorId).getName();
        System.out.println(vendorName);
        System.out.println(vendorId);
        List<VendorWorkplaceShorten> returnList = new ArrayList<>();
        for (VendorWorkplace workplace : ListOfWorkplaces) {
            VendorWorkplaceShorten shorten = new VendorWorkplaceShorten();
            shorten.setAddress(workplace.getFullAddress());
            shorten.setType(workplace.getType());
            shorten.setCreatedAt(workplace.getCreatedAt());
            returnList.add(shorten);
        }
        return new ResponseEntity<>(VendorWorkplaceReturnParam.builder()
                .success(true)
                .vendorId(vendorId)
                .vendorName(vendorName)
                .rowData(returnList)
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<VendorWorkplaceReturnParam> addWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        System.out.println(receiveParam);
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
        String KAKAO_SERVICE_TYPE = "kakao";

        String url = "https://dapi.kakao.com/v2/local/search/address.json";

        String accessKey = "KakaoAK 1af1d849e0fc9601d683f43794c1ed35";

        VendorWorkplace newWorkplace = receiveParamToWorkPlace(receiveParam);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", accessKey);
        String refineUrl = url + "?query=" + receiveParam.getJibunAddress();
        HttpEntity request = new HttpEntity<>(headers);
        String dataResult;
        Double latitude;
        Double longitude;
        System.out.println(newWorkplace);
        try {
            dataResult = restTemplate.exchange(refineUrl, HttpMethod.GET, request, String.class).getBody();
            System.out.println(dataResult);
            Map result = objectMapper.readValue(dataResult, Map.class);
            List<Map> docs = (ArrayList) result.get("documents");
            System.out.println(docs);
            System.out.println(docs.get(0));
            Map doc = docs.get(0);
            latitude = Double.parseDouble((String) doc.get("y"));
            longitude = Double.parseDouble((String) doc.get("x"));
            System.out.println(latitude);
            System.out.println(longitude);
            newWorkplace.setLatitude(latitude);
            newWorkplace.setLongitude(longitude);
            System.out.println(newWorkplace);
        } catch (IOException exception) {
            //what each log levels do (whether they stop the application or so)
            System.out.println("위도 경도 정보 받을수 없음");
            log.warn("위도 경도 받기 불가");
//            throw new ArgoBizException("위도 경도 검색 실패");
        }

        vendorWorkplaceRepository.saveAndFlush(newWorkplace);
//        try {
//        dataResult = restTemplate.exchange(refineUrl, HttpMethod.GET, request, String.class).getBody();
//        } catch (Exception e) {
//            dataResult = null;
//            log.warn("정제할 수 없는 주소 : {}", receiveParam.getJibunAddress());
//        }
//        vendorWorkplaceRepository.saveAndFlush(workplace);
        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .vendorId(newWorkplace.getVendor().getVendorId())
                .success(true)
                .build(), HttpStatus.OK);
    }

    public VendorWorkplace receiveParamToWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        VendorWorkplace newWorkplace = new VendorWorkplace();
////        ObjectMapper objectMapper = new ObjectMapper();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        headers.add("Authorization", accessKey);
////        String refineUrl = url + "?query=" + receiveParam.getJibunAddress();
////        HttpEntity request = new HttpEntity<>(headers);
////        String dataResult;
////        Double latitude;
////        Double longitude;
////        try {
////            dataResult = restTemplate.exchange(refineUrl, HttpMethod.GET, request, String.class).getBody();
////            System.out.println(dataResult);
////            Map result = objectMapper.readValue(dataResult, Map.class);
////            List<Map> docs = (ArrayList) result.get("documents");
////            Map doc = docs.get(0);
////            latitude = Double.parseDouble((String) doc.get("y"));
////            longitude = Double.parseDouble((String) doc.get("x"));
////        } catch(IOException exception) {
////            throw new ArgoBizException("위도 경도 검색 실패");
////        }
//
        newWorkplace.setVendor(vendorService.getVendor(receiveParam.getVendorId()));
        newWorkplace.setType(receiveParam.getType());
        newWorkplace.setZipCode(receiveParam.getZipCode());
        newWorkplace.setPostCode(receiveParam.getPostCode());
        newWorkplace.setFullAddress(receiveParam.getFullAddress());
        newWorkplace.setJibunAddress(receiveParam.getJibunAddress());
        newWorkplace.setJibunAddressEnglish(receiveParam.getJibunAddressEnglish());
//        newWorkplace.setLatitude(latitude);
//        newWorkplace.setLongitude(longitude);
        newWorkplace.setRoadAddress(receiveParam.getRoadAddress());
        newWorkplace.setRoadAddressEnglish(receiveParam.getRoadAddressEnglish());
        newWorkplace.setNationlInfo(receiveParam.getNationalInfo());
        return newWorkplace;
    }
}