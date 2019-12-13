package com.argo.common.domain.vendor;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VendorWorkplaceService {
    @Autowired
    VendorWorkplaceRepository vendorWorkplaceRepository;

    public ResponseEntity<VendorWorkplaceReturnParam> getListPerVendor(Long workPlaceId) {
        VendorWorkplace selectedVendor =
                vendorWorkplaceRepository.findVendorWorkplaceByVendorWorkplaceId(workPlaceId);
//        List<VendorWorkplace> vendorWorkplaceList = vendorWorkplaceRepository.findAllByVendorID
//         for (VendorWorkPlace vendorWorkPlace : vendorWorkplaceList) {
//         type selection
//    }
        VendorWorkplaceReturnParam shorten = new VendorWorkplaceReturnParam();
        shorten.setType(selectedVendor.getType());
        shorten.setAddress(selectedVendor.getVendorOfficeAddress());
        shorten.setNation(selectedVendor.getVendorNation());
        shorten.setCreatedAt(selectedVendor.getCreatedAt());
        List<VendorWorkplaceReturnParam> rowData  = new ArrayList<>();
        rowData.add(shorten);
       return new ResponseEntity<>(VendorWorkplaceReturnParam.builder()
               .success(true)
               .type(selectedVendor.getType())
               .nation(selectedVendor.getVendorNation())
               .address(selectedVendor.getVendorOfficeAddress())
               .rowData(rowData)
               .build(), HttpStatus.OK);
    }
}