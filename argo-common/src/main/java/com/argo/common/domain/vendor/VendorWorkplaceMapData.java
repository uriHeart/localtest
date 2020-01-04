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

    public static VendorWorkplaceMapData from(VendorWorkplace data) {
        VendorWorkplaceMapData mapData = new VendorWorkplaceMapData();
        mapData.setVendorWorkplaceId(data.getVendorWorkplaceId());
        mapData.setWorkplaceName(data.getWorkplaceName());
        mapData.setFullAddress(data.getFullAddress());
        Double latitude = data.getUserSelectedLatitude();
        Double longitude = data.getUserSelectedLongitude();
        System.out.println(latitude);
        System.out.println(longitude);
        if (latitude == null || longitude == null) {
            latitude = data.getAdminSelectedLatitude();
            longitude = data.getAdminSelectedLongitude();
        }
        mapData.setLatitude(latitude);
        mapData.setLongitude(longitude);
        return mapData;
    }
}
