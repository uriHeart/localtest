package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TwentyNineCollector extends AbstractOrderCollector {

    @Override
    public boolean isSupport(String channel) {
        return "TWENTY_NINE_CM".equals(channel);
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
        headers.add("cookie", authorization);
        headers.add("referer", "https://partner.29cm.co.kr/cs/order-list");


        dataUrl = dataUrl + "?start_date=" + ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1))
                + "&end_date=" + ArgoDateUtil.getDateString(LocalDate.now())
                + "&limit=1000&offset=0";

        HttpEntity request = new HttpEntity<>(headers);
        String dataResult = restTemplate.exchange(dataUrl, HttpMethod.GET, request, String.class).getBody();
        try {
            Map data = objectMapper.readValue(dataResult, Map.class);
            List<Map> rawEvents = (List<Map>) data.get("results");
            rawEvents.forEach(
                    event -> {
                        try {
                            RawEvent rawEvent = RawEvent.builder()
                                    .vendorId(channel.getVendor().getVendorId())
                                    .channelId(channel.getSalesChannel().getSalesChannelId())
                                    .format("JSON")
                                    .auto(true)
                                    .data(objectMapper.writeValueAsString(event))
                                    .orderId(event.get("order_no").toString())
                                    .publishedAt(ArgoDateUtil.getDate(event.get("place_order_timestamp").toString().replaceAll("\\.", "-")))
                                    .createdAt(new Date())
                                    .build();
                            rawEventService.save(rawEvent);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
            );
            log.info("data - {}", rawEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
