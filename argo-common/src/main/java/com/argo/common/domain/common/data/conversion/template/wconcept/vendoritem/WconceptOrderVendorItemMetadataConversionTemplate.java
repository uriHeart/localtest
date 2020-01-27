package com.argo.common.domain.common.data.conversion.template.wconcept.vendoritem;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WconceptOrderVendorItemMetadataConversionTemplate {
    public static ConversionTemplate getOrderVendorItemMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("6-OrderVendorItemMetadata")
                .targetId("com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata")
                .rules(getConversionRuleForOrderVendorItemMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderVendorItemMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        Map<String, String> amtParam = Maps.newLinkedHashMap();
        amtParam .put("판매가", "java.lang.String");

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("amountFormatConversionService")
                .operatorMethod("replaceComa")
                .operatorParams(amtParam)
                .targetField("originalPrice")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("amountFormatConversionService")
                .operatorMethod("replaceComa")
                .operatorParams(amtParam)
                .targetField("salesPrice")
                .build());

        return list;
    }
}
