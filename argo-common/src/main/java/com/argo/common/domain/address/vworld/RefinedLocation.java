package com.argo.common.domain.address.vworld;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefinedLocation {
    private String crs;
    private RefinedPoint point;
}
