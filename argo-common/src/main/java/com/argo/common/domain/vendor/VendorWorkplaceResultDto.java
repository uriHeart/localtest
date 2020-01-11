package com.argo.common.domain.vendor;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VendorWorkplaceResultDto {
    private boolean success;
    private Long vendorId; //;
    private Long vendorWorkPlaceId;
    private String type;
    private String address;
    private String nation;
    private Date createdAt;
    private Double latitude;
    private Double longitude;
    private Long workplaceId;
    private String message;
    private List<VendorWorkplaceShorten> rowData;
    private List<VendorWorkplaceMapData> mapData;
}
