package com.argo.api.controller.convert;

import com.argo.api.service.ConvertService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ConvertController {

    @Autowired
    ConvertService convertService;

    @Autowired
    HttpSession httpSession;

    @RequestMapping(value="/excelUpload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody String excelUpload(@RequestParam("file")MultipartFile parts,
                                            @RequestParam("channelId")Long channelId,
                                            @RequestParam("vendorId")Long vendorId) throws IOException {
        //엑셀저장
        File excel = convertService.excelUpload(parts);

        //엑셀데이터 json변환  List<엑셀sheet List<레코드 >
        List<HashMap<String, Object>> jsonData = convertService.excelToJson(excel);

        RestStatus restStatus = convertService.saveToEs(excel.getName(), jsonData,vendorId);

        List<HashMap<String, String>> factorAddJsonData = convertService.addExcelFactor(jsonData,channelId);

        //카산드라에 raw데이터 저장
        convertService.saveToCassandra((List<HashMap<String, String>>) factorAddJsonData,channelId,vendorId);

        return restStatus.name();
    }


    @RequestMapping(value="/excel",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf8")
    public @ResponseBody Boolean existsExcel(@RequestParam("fileName")String fileName) throws IOException {
        boolean excelList = convertService.existsExcel(fileName);
        return excelList;
    }

    @GetMapping(value="/excel/list",
            produces = "application/json; charset=utf8")
    public @ResponseBody List<Map<String,Object>> getUploadList() throws IOException {
        //TODO userId에 해당하는 업로드파일만 조회
        //HttpSession session = req.getSession();
        //String userId = (String)session.getAttribute("userId");
        List<Map<String,Object>> excelList = convertService.getExcelList("");
        return excelList;
    }

    /**
     * @param indexId
     * @return Object
     * @throws IOException
     */
    @RequestMapping(value="/excel/detail",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf8")
    public @ResponseBody Object getExcelMainText(@RequestParam String indexId) throws IOException {
        ArrayList<HashMap<String, HashMap<String, Object>>> excelMainText = convertService.getExcelMainText(indexId);
        return excelMainText;
    }

}
