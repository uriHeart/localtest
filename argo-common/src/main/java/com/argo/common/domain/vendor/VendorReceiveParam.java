package com.argo.common.domain.vendor;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorReceiveParam {
    private Long vendorId; //;
    private Long type;
    private String address;
}
