package com.argo.common.domain.common.data.conversion.template.kasina.offline.vendoritem;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KasinaOfflineOrderVendorItemMetadataConversionTemplate {
    public static ConversionTemplate getOrderVendorItemMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("16-ORDER-OrderVendorItemMetadata")
                .targetId("com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata")
                .rules(getConversionRuleForOrderVendorItemMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderVendorItemMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("소비자가격")
                .targetField("originalPrice")
                .build());
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("판매가")
                .targetField("salesPrice")
                .build());
        return list;
    }
}
