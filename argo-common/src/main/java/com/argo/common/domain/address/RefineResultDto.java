package com.argo.common.domain.address;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefineResultDto {
    private String originalAddressHash;
    private OriginalAddressDto originalAddress;
    private RefinedAddressDto refinedAddress;
}
