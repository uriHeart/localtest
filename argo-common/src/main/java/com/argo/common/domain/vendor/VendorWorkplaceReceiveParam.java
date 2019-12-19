package com.argo.common.domain.vendor;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorWorkplaceReceiveParam {
    private Long vendorId; //;
    private String type;
    private String fullAddress;
    private String jibunAddress;
    private String jibunAddressEnglish;
    private String roadAddress;
    private String roadAddressEnglish;
//    private String extraAddress;
    private String zipCode;
    private String postCode;
    private String nationalInfo;
}
