package com.argo.common.domain.vendor;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorWorkplaceReturnParam {
    private boolean success;
    private Long vendorId; //;
    private Long vendorWorkPlaceId;
    private String type;
    private String address;
    private String nation;
    private Date createdAt;
    private List<VendorWorkplaceReturnParam> rowData;
}
