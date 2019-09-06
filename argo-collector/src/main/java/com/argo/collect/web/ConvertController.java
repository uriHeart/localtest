package com.argo.collect.web;


import com.argo.collect.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ConvertController {

    @Autowired
    ConvertService convertService;

    @RequestMapping(value="/excelUpload",
                    method = RequestMethod.POST,
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> excelUpload(@RequestParam("file")MultipartFile parts,
                                      @RequestParam("channelId")Long channelId,
                                      @RequestParam("vendorId")Long vendorId)throws IOException {
        //엑셀저장
        File excel = convertService.excelUpload(parts);

        //엑셀데이터 json변환  List<엑셀sheet List<레코드 >
        List<List<HashMap<String,String>>> jsonData = convertService.excelToJson(excel);

        //TODO
        //엑셀 헤더를 저장할데이터의 key값으로 맵핑 설정하는 기능 ex)주문번호 =>oderNo

        //카산드라에 raw데이터 저장
        convertService.saveToCassandra(jsonData,channelId,vendorId);


        return new ResponseEntity<String>("upload success", HttpStatus.OK);

        //TO_DO
        //excel => json 변환
        //json data => 카산드라 , 엘라스틱서치
        //파일명 DB저장

    }

}
