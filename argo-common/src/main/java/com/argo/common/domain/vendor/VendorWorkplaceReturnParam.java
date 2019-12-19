package com.argo.common.domain.vendor;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorWorkplaceReturnParam {
    private boolean success;
    private Long vendorId; //;
    private String vendorName;
    private List<VendorWorkplaceShorten> rowData;
}
