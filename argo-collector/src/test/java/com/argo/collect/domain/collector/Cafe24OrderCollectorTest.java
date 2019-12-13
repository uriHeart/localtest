package com.argo.collect.domain.collector;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
public class Cafe24OrderCollectorTest extends AbstractOrderCollector {

    @Autowired
    private VendorService vendorService;

    @Override
    public boolean isSupport(String channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel channel) {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        AuthorityManager authorityManager = super.getAuth(channel.getSalesChannel().getCode());
        String authorization = authorityManager.requestAuth(channel);
        CollectParam collectParam = super.getCollectInfo(channel);

        String dataUrl = collectParam.getCollectUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", authorization);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("S_SDATE", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.add("S_EDATE", ArgoDateUtil.getDateString(LocalDate.now()));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String dataResult = restTemplate.postForObject(dataUrl, request , String.class);
        try {
            Map data = objectMapper.readValue(dataResult, Map.class);
            List<Map> rawEvents = (List<Map>) data.get("data");
            rawEvents.forEach(
                event -> {
                    try {
                        RawEvent rawEvent = RawEvent.builder()
                                .vendorId(channel.getVendor().getVendorId())
                                .channelId(channel.getSalesChannel().getSalesChannelId())
                                .format("JSON")
                                .auto(true)
                                .data(objectMapper.writeValueAsString(event))
                                .orderId(event.get("ord_no").toString())
                                .publishedAt(ArgoDateUtil.getDate(event.get("ord_date").toString().replaceAll("\\.", "-")))
                                .createdAt(new Date())
                                .build();
                        rawEventService.save(rawEvent);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            );
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
