package com.argo.common.domain.common.data.conversion.template.musinsa.vendoritem;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusinsaOrderVendorItemMetadataConversionTemplate {
    public static ConversionTemplate getOrderVendorItemMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8-OrderVendorItemMetadata")
                .targetId("com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata")
                .rules(getConversionRuleForOrderVendorItemMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderVendorItemMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("price")
                .targetField("originalPrice")
                .build());
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("price")
                .targetField("salesPrice") //to_do 환불때 *-1 로입력
                .build());

        return list;
    }
}
