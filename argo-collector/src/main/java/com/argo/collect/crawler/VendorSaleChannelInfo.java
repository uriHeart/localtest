package com.argo.collect.crawler;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class VendorSaleChannelInfo {
    private String vendorCode;
    private String saleChannelCode;

    private String vendorName;
    private Long businessNumber;

    private Set<String> keywords;
    private boolean findAll;
}

