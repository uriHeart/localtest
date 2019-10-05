package com.argo.collect.domain.service;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.service.ConvertService;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ConvertServiceTest {

    @Autowired
    ConvertService convertService;

    @Autowired
    RawEventService rawEventService;

    @Test
    public void existsExcel() throws IOException {
        boolean result =  convertService.existsExcel("플레이어3월.xlsx");
        System.out.println(result);
    }

    @Test
    public void cassandraUTF8() throws IOException {

        String value ="한글";
        //String orderId = new String(value.getBytes(ISO_8859_1), UTF_8);
        String orderId = new String(value.getBytes(UTF_8));


        String inputText = "한글1";
        InputStream is = new ByteArrayInputStream(inputText.getBytes("UTF-8"));
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
        }
        rd.close();

        RawEvent rawEvent
                = RawEvent.builder()
                .vendorId(new Long(1))
                .channelId(new Long(1))
                .orderId(orderId)
                .publishedAt(new Date())
                .format("JSON")
                .auto(false)
                .data(response.toString())
                .createdAt(new Date())
                .build();
        rawEventService.save(rawEvent);


    }

}
