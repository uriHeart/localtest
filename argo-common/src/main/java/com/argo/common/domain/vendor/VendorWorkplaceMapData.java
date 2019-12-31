package com.argo.common.domain.vendor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorWorkplaceMapData {
    public Long vendorId;
    public Long vendorWorkplaceId;

    // 둘중에 label 에 뭐 표시할지 결정 필요
    public String workplaceName;
    public String fullAddress;

    public Double latitude;
    public Double longitude;
}
