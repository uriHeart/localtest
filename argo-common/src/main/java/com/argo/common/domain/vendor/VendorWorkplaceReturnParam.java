package com.argo.common.domain.vendor;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorWorkplaceReturnParam {
    private boolean success;
    private Long vendorId; //
    private String vendorName; // front에 보여야할지...
    private Double longitude;
    private Double latitude; // location object 만드는 대신 그냥 simple 하게 이렇게 넘겨줌.
    private List<VendorWorkplaceShorten> rowData;
}
