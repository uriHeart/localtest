package com.argo.api.service;


import com.argo.api.ArgoApiApplication;
import com.argo.api.domain.excel.ChannelExcelMappingService;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoApiApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ConvertServiceTest {


    @Value("${file.upload.default-path}")
    private String path;

    @Value("${file.upload.index}")
    private String excelIndex;

    @Autowired
    RawEventService rawEventService;

    @Autowired
    Gson gson;

    @Autowired
    HttpSession httpSession;

    @Autowired
    private RestHighLevelClient esClient;

    @Autowired
    ChannelExcelMappingService channelExcelMappingService;

    @Autowired
    ObjectMapper objectMapper;



    public Map getExcelMainText(String indexId) throws IOException {
            GetRequest getRequest = new GetRequest(excelIndex,indexId);
            GetResponse getResponse = esClient.get(getRequest, RequestOptions.DEFAULT);
            Map data =  getResponse.getSourceAsMap();
            List<HashMap> rowDatakeyList = (List<HashMap>) data.get("rowDatakeyList");
            List<HashMap> rowDataList = new ArrayList<>();
            rowDatakeyList.forEach(row ->{

                Long vandorId = Long.parseLong(row.get("vendorId").toString());
                Long channelId = Long.parseLong(row.get("channelId").toString());
                String orderId = row.get("orderId").toString();
                Date publishedAt = ArgoDateUtil.getDateBy24H(row.get("publishedAt").toString());

                RawEvent rawEvent = rawEventService.getRawEvent(vandorId,channelId,orderId,publishedAt);

                String rawData = null;

                if(rawEvent != null){
                    rawData = rawEvent.getData();
                }
                Map raw = null;
                try {
                    raw = objectMapper.readValue(rawData, Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                rowDataList.addAll((Collection<? extends HashMap>) raw.get("datas"));
            });

        Map result = new HashMap();
        result.put("sheetData",rowDataList);
        result.put("sheetHeader",data.get("sheetHeader"));
        result.put("sheetName",data.get("sheetName"));

        return result;
    }


    @Test
    public void exec() throws IOException {
        getExcelMainText("골스토어_매출_상세내역.xlsx");
    }





}
