package com.argo.address.service;

import com.argo.common.dto.SearchResult;
import com.argo.common.configuration.ArgoBizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AddressRefiner {
    @Value("${refine.url}")
    private String url;

    @Value("${refine.access-key}")
    private String accessKey;

    @Autowired
    private RestTemplate restTemplate;

    public SearchResult refine(String address) {
        for (SearchType type : SearchType.values()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            builder.queryParam("key", accessKey);
            builder.queryParam("address", address);
            builder.queryParam("type", type.name().toLowerCase());
            String uriBuilder = builder.build().encode().toUriString();
            try {
                SearchResult result = restTemplate.getForObject(new URI(uriBuilder), SearchResult.class);
                if (result != null && "ERROR".equals(result.getResponse().getStatus())) {
                    throw new ArgoBizException(result.getResponse().getError().getText());
                }
                if (result != null && "OK".equals(result.getResponse().getStatus())) {
                    return result;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new ArgoBizException(" 주소 정제 호출에 실패했습니다.", e);
            }
        }
        return null;
    }
}
