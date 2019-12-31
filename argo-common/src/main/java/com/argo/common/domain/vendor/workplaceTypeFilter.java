package com.argo.common.domain.vendor;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class workplaceTypeFilter {
    //Front에서 종류 번호로 받을 것.
    public VendorWorkplaceType receiverFilter(Long typeNum) {
        if (typeNum == 1) {
            return VendorWorkplaceType.WAREHOUSE;
        } else if (typeNum == 2) {
            return VendorWorkplaceType.STORE;
        } else if (typeNum == 3) {
            return VendorWorkplaceType.OFFICE;
        } else {
            return VendorWorkplaceType.ETC;
        }
    }
    public String toKorean(VendorWorkplaceType type, String etcDetail) {
        if (type == VendorWorkplaceType.WAREHOUSE) {
            return "물류창고";
        } else if (type == VendorWorkplaceType.STORE) {
            return "매장";
        } else if (type == VendorWorkplaceType.OFFICE) {
            return "사무실";
        } else {
            if (!etcDetail.equals("")) {
                return "기타" + " (" + etcDetail + ")";
            } else {
                return "기타";
            }
        }
    };

}
