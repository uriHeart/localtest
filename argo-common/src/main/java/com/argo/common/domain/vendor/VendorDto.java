package com.argo.common.domain.vendor;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorDto {
    private Long vendorId;
    private String vendorName;
}
