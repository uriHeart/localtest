package com.argo.common.domain.vendor;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum VendorWorkplaceType {
    WAREHOUSE, //물류창고 (1)
    STORE, //매장 (2)
    OFFICE,//사무실 (3)
    ETC,//기타 (4)
};
