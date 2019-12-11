package com.argo.common.domain.address.vworld;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResponse {
    private AddressInput input;
    private RefinedAddress refined;
    private RefinedLocation result;
    private String status;
    private SearchError error;
}
