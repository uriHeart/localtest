package com.argo.common.domain.address.kakao;

import com.argo.common.configuration.ArgoBizException;
import com.argo.common.domain.address.AbstractAddressRefiner;
import com.argo.common.domain.address.OriginalAddressDto;
import com.argo.common.domain.address.RefineResultDto;
import com.argo.common.domain.address.RefinedAddressDto;
import com.argo.common.domain.address.vworld.SearchType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KakaoAddressRefiner extends AbstractAddressRefiner {
    private final static String KAKAO_SERVICE_TYPE = "kakao";

    @Value("${refine.kakao.url}")
    private String url;

    @Value("${refine.kakao.access-key}")
    private String accessKey;

    @Override
    public RefineResultDto refine(String address) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", accessKey);
        String refineUrl = url + "?query=" + address;

        HttpEntity request = new HttpEntity<>(headers);
        String dataResult;

        try {
            dataResult = restTemplate.exchange(refineUrl, HttpMethod.GET, request, String.class).getBody();
        } catch (Exception e) {
            dataResult = null;
            log.warn("정제할 수 없는 주소 : {}", address);
        }

        if (dataResult == null)  {
            return null;
        }

        try {
            Map result = objectMapper.readValue(dataResult, Map.class);
            Map meta = (Map) result.get("meta");
            if ((Integer)meta.get("total_count") == 1) {
                List<Map> docs = (ArrayList) result.get("documents");
                Map doc = docs.get(0);
                return RefineResultDto.builder()
                        .originalAddress(OriginalAddressDto.builder()
                                .fullAddress(address)
                                .build())
                        .refinedAddress(RefinedAddressDto.builder()
                                .latitude(Double.parseDouble((String) doc.get("y")))
                                .longitude(Double.parseDouble((String) doc.get("x")))
                                .build())
                        .build();
            }

            log.info("result : {}", result);
        } catch (IOException e) {
            throw new ArgoBizException("카카오 주소 정제 결과 파싱 실패", e);
        }

        return null;
    }

    @Override
    public boolean isTargetService() {
        return KAKAO_SERVICE_TYPE.equals(super.serviceType);
    }
}
