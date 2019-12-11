package com.argo.common.domain.address.vworld;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchError {
    private String level;
    private String code;
    private String text;
}
