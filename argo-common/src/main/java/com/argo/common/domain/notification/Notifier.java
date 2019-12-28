package com.argo.common.domain.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class Notifier {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${notification.slack}")
    private String slackHookUrl;

    public void send(Throwable e) {
       String errorMessage = ExceptionUtils.getStackTrace(e);
       send(errorMessage);
    }

    public void send(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("text", message);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String result = restTemplate.postForObject(slackHookUrl, request , String.class);

        log.info("send result : {}", result);
    }
}
