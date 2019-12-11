package com.argo.common.domain.address;

import com.google.common.hash.Hashing;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public abstract class AbstractAddressRefiner implements AddressRefiner {

    @Value("${refine.service-type}")
    protected String serviceType;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private ArgoRefineAddressRepository argoRefineAddressRepository;

    @Override
    public final RefineResultDto addressRefine(String address) {
        String originalAddressHash = Hashing.sha256().hashString(address, StandardCharsets.UTF_8).toString();
        Date targetDate = LocalDate.now().toDate();
        // 오늘 날짜 정제 결과 있는지 조회 - 있으면 리턴 없으면 정제 시도
        ArgoRefineAddress refineAddress =  argoRefineAddressRepository.findByOriginalAddressHashAndRefineDate(originalAddressHash, targetDate);
        if (refineAddress != null) {
            return RefineResultDto.builder()
                    .originalAddressHash(originalAddressHash)
                    .originalAddress(OriginalAddressDto.builder()
                            .fullAddress(refineAddress.getOriginalAddress())
                            .build())
                    .refinedAddress(RefinedAddressDto.builder()
                            .latitude(refineAddress.getLatitude())
                            .longitude(refineAddress.getLongitude())
                            .build())
                    .build();
        }

        RefineResultDto result = refine(address);
        if (result != null) {
            result.setOriginalAddressHash(originalAddressHash);
            argoRefineAddressRepository.save(ArgoRefineAddress.builder()
                    .originalAddressHash(originalAddressHash)
                    .refineDate(targetDate)
                    .originalAddress(address)
                    .latitude(result.getRefinedAddress().getLatitude())
                    .longitude(result.getRefinedAddress().getLongitude())
                    .build());
        }

        return result;
    }

    protected abstract RefineResultDto refine(String address);
}
