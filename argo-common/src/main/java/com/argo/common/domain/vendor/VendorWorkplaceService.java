package com.argo.common.domain.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VendorWorkplaceService {

    private final static String KAKAO_SERVICE_TYPE = "kakao";

    @Value("${refine.kakao.url}")
    private String url;

    @Value("${refine.kakao.access-key}")
    private String accessKey;

    @Autowired
    VendorWorkplaceRepository vendorWorkplaceRepository;

    @Autowired
    VendorService vendorService;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<VendorWorkplaceReturnParam> getListPerVendor(Long vendorId) {
        List<VendorWorkplace> ListOfWorkplaces =
                vendorWorkplaceRepository.findAllByVendorOrderByType(vendorService.getVendor(vendorId));

        String vendorName = vendorService.getVendor(vendorId).getName();
        System.out.println(vendorName);
        System.out.println(vendorId);
        List<VendorWorkplaceShorten> returnList  = new ArrayList<>();
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
//        VendorWorkplace workplace =
//                VendorWorkplace.builder()
//                .vendor(vendorService.getVendor(receiveParam.getVendorId()))
//                .fullAddress(receiveParam.getAddress())
//                .type(receiveParam.getType())
//                .jibunAddress(receiveParam.getJibunAddress())
//                .jibunAddressEnglish(receiveParam.getJibunAddressEnglish())
//                .roadAddress(receiveParam.getRoadAddress())
//                .roadAddressEnglish(receiveParam.getRoadAddressEnglish())
//                .postCode(receiveParam.getPostCode())
//                .zipCode(receiveParam.getZipCode())
//                .nationlInfo(receiveParam.getNationalInfo())
//                .createdAt(new Date())
//                .build();
//        vendorWorkplaceRepository.saveAndFlush(workplace);
        VendorWorkplace workplace = receiveParamToWorkPlace(receiveParam);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", accessKey);
        String refineUrl = url + "?query=" + receiveParam.getJibunAddress();
        HttpEntity request = new HttpEntity<>(headers);
        String dataResult;
        dataResult = restTemplate.exchange(refineUrl, HttpMethod.GET, request, String.class).getBody();
        System.out.println(dataResult);
//        try {
//        dataResult = restTemplate.exchange(refineUrl, HttpMethod.GET, request, String.class).getBody();
//        } catch (Exception e) {
//            dataResult = null;
//            log.warn("정제할 수 없는 주소 : {}", receiveParam.getJibunAddress());
//        }

        vendorWorkplaceRepository.saveAndFlush(workplace);
        return new ResponseEntity<>(VendorWorkplaceReturnParam
                .builder()
                .vendorId(workplace.getVendor().getVendorId())
                .success(true)
                .build(), HttpStatus.OK);
    }

    public VendorWorkplace receiveParamToWorkPlace(VendorWorkplaceReceiveParam receiveParam) {
        VendorWorkplace newWorkplace = new VendorWorkplace();
        newWorkplace.setVendor(vendorService.getVendor(receiveParam.getVendorId()));
        newWorkplace.setType(receiveParam.getType());
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