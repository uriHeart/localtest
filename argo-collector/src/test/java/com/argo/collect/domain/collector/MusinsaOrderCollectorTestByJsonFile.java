package com.argo.collect.domain.collector;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class MusinsaOrderCollectorTestByJsonFile {

    @Autowired
    protected RawEventService rawEventService;

    @Test
    public void collect()  {
        ObjectMapper objectMapper = new ObjectMapper();

        FileReader dataResult = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\project\\argo_server_dev\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\musinsaConversionTestData"),"UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            dataResult = new FileReader("C:\\project\\argo_server_dev\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\musinsaConversionTestData");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Map data = objectMapper.readValue(in, Map.class);

            RawEvent rawEvent = RawEvent.builder()
                    .vendorId(1L)
                    .channelId(8L)
                    .data(objectMapper.writeValueAsString(data))
                    .event(String.valueOf(data.get("eventType")))
                    .format("JSON")
                    .auto(true)
                    .createdAt(new Date())
                    .orderId("201912131647275847")
                    .publishedAt(ArgoDateUtil.getDate("2019.12.16 16:55:12".replaceAll("\\.", "-")))
                    .build();
            rawEventService.save(rawEvent);


        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
