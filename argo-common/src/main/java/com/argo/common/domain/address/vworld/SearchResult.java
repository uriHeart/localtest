package com.argo.common.domain.address.vworld;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResult {
    private SearchResponse response;
}
