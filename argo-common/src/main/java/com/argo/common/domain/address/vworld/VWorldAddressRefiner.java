package com.argo.common.domain.address.vworld;

import com.argo.common.configuration.ArgoBizException;
import com.argo.common.domain.address.AbstractAddressRefiner;
import com.argo.common.domain.address.OriginalAddressDto;
import com.argo.common.domain.address.RefineResultDto;
import com.argo.common.domain.address.RefinedAddressDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class VWorldAddressRefiner extends AbstractAddressRefiner {
    private final static String VWORLD_SERVICE_TYPE = "kakao";

    @Value("${refine.vworld.url}")
    private String url;

    @Value("${refine.vworld.access-key}")
    private String accessKey;

    @Override
    public RefineResultDto refine(String address) {
        for (SearchType type : SearchType.values()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            builder.queryParam("key", accessKey);
            builder.queryParam("address", address);
            builder.queryParam("type", type.name().toLowerCase());
            String uriBuilder = builder.build().encode().toUriString();
            try {
                SearchResult searchResult = restTemplate.getForObject(new URI(uriBuilder), SearchResult.class);
                if (searchResult != null && "ERROR".equals(searchResult.getResponse().getStatus())) {
                    throw new ArgoBizException(searchResult.getResponse().getError().getText());
                }
                if (searchResult != null && "OK".equals(searchResult.getResponse().getStatus())) {
                    String searchType = searchResult.getResponse().getInput().getType();
                    RefinedAddress refinedAddress = searchResult.getResponse().getRefined();
                    RefinedLocation location = searchResult.getResponse().getResult();
                    return RefineResultDto.builder()
                            .originalAddress(OriginalAddressDto.builder()
                                    .fullAddress(address)
                                    .build())
                            .refinedAddress(RefinedAddressDto.builder()
                                    .roadAddress(searchType.equals(SearchType.PARCEL.name().toLowerCase())
                                            ? null : refinedAddress.getText())
                                    .jibunAddress(searchType.equals(SearchType.PARCEL.name().toLowerCase())
                                            ? refinedAddress.getText() : null)
                                    .latitude(Double.parseDouble(location.getPoint().getY()))
                                    .longitude(Double.parseDouble(location.getPoint().getX()))
                                    .build())
                            .build();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new ArgoBizException(" 주소 정제 호출에 실패했습니다.", e);
            }
        }
        return null;
    }

    @Override
    public boolean isTargetService() {
        return VWORLD_SERVICE_TYPE.equals(super.serviceType);
    }
}
