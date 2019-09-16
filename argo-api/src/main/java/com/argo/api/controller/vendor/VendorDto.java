package com.argo.api.controller.vendor;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorDto {
    private Long vendorId;
    private String vendorName;
}
