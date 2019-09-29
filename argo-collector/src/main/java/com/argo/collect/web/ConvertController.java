package com.argo.collect.web;

import com.argo.collect.service.ConvertService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class ConvertController {

    @Autowired
    ConvertService convertService;

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

        //현재 동기호출이 안됨.
        RestStatus restStatus = convertService.saveToEs(excel.getName(), jsonData);

        //카산드라에 raw데이터 저장
        //convertService.saveToCassandra(jsonData,channelId,vendorId);

        return restStatus.name();
    }

    @RequestMapping(value="/excel/list",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf8")
    public @ResponseBody List<Map<String,Object>> getUploadList(HttpServletRequest req) throws IOException {
        //TODO userId에 해당하는 업로드파일만 조회
        //HttpSession session = req.getSession();
        //String userId = (String)session.getAttribute("userId");
        List<Map<String,Object>> excelList = convertService.getExcelList("");
        return excelList;
    }

    /**
     *TODO
     * 업로드한 엑셀의 헤더와 본문내용을 가져와 front의 그리드에 출력한다.
     * 별도로 엑셀 문서마다 헤더 맵핑을 하지않고 업로드한 엑셀 형식을 동적으로 보여준다.
     * 차후 저장 할 엑셀문서의 양식이 확정되면 헤더맵핑한다.
     *
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
