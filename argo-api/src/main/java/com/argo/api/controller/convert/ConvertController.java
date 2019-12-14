package com.argo.api.controller.convert;

import com.argo.api.common.ArgoResponse;
import com.argo.api.service.ConvertService;
import com.argo.common.configuration.ArgoBizException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

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
    public @ResponseBody
    ResponseEntity<ArgoResponse> excelUpload(@RequestParam("file")MultipartFile parts,
                                             @RequestParam("channelId")Long channelId,
                                             @RequestParam("vendorId")Long vendorId) throws IOException, ArgoBizException {
        //엑셀저장
        File excel = convertService.excelUpload(parts);

        try{
            //엑셀데이터 json변환  List<엑셀sheet List<레코드 >
            List<HashMap<String, Object>> jsonData = convertService.excelToJson(excel);

            //엑셀 맵핑 체크
            convertService.addExcelFactorCheck(jsonData,channelId);

            RestStatus restStatus = convertService.saveToEs(excel.getName(), jsonData,vendorId,channelId);


        }catch (ArgoBizException e){
            return new ResponseEntity<>(ArgoResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>(ArgoResponse.builder().message("ok").build(), HttpStatus.ACCEPTED);
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

    @RequestMapping(value="/excel/detail",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf8")
    public @ResponseBody Object getExcelMainText(@RequestParam String indexId) throws IOException {
        ArrayList<HashMap<String, HashMap<String, Object>>> excelMainText = convertService.getExcelMainText(indexId);
        return excelMainText;
    }

//    /**
//     * @param indexId
//     * @return Object
//     * @throws IOException
//     */
//    @RequestMapping(value="/excel/detail",
//            method = RequestMethod.GET,
//            produces = "application/json; charset=utf8")
//    public @ResponseBody Object getExcelMainText(@RequestParam String indexId) throws IOException {
//        Map excelMainText = convertService.getExcelMainText(indexId);
//            ArrayList<Map> result = new ArrayList<>();
//            result.add(excelMainText);
//        return result;
//    }

    @DeleteMapping(value="/excel/delete")
    public @ResponseBody Mono<RestStatus> deleteIndex(@RequestParam String docId){
        Mono<RestStatus> restStatus = convertService.deleteIndex(docId);
        return restStatus;
    }

    @PutMapping(value="/excel/event/confirm")
    public @ResponseBody ResponseEntity<ArgoResponse> eventConfirm(@RequestParam String docId,Long channelId,Long vendorId) throws IOException {
        ArrayList<HashMap<String,Object>> eventData = convertService.getEventList(docId);

        ArrayList<HashMap<String,Object>> factorAddJsonData = convertService.addExcelFactor(eventData,channelId);
        //카산드라에 raw데이터 저장
        convertService.saveToCassandra(factorAddJsonData,channelId,vendorId);

        return new ResponseEntity<>(ArgoResponse.builder().message("ok").build(), HttpStatus.ACCEPTED);
    }

}