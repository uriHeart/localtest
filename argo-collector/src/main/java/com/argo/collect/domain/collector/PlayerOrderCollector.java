package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.auth.AuthorityParam;
import com.argo.collect.domain.enums.SalesChannel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PlayerOrderCollector extends AbstractOrderCollector {
    @Override
    public boolean isSupport(SalesChannel channel) {
        return SalesChannel.PLAYER == channel;
    }

    @Override
    public void collect(SalesChannel channel) {
        RestTemplate restTemplate = new RestTemplate();
        AuthorityManager authorityManager = super.getAuth(channel);
        String authData = authorityManager.requestAuth(AuthorityParam.builder().build());

        String dataUrl = "http://biz.player.co.kr/po/order/ord01/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", authData);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("S_SDATE", "2019-07-18");
        map.add("S_EDATE", "2019-08-18");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String dataResult = restTemplate.postForObject(dataUrl, request , String.class);

        // cas insert
    }
}
