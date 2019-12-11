package com.argo.common.domain.common.data.conversion.template.offline.hyundai.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HyundaiOrderMetadataConversionTemplate {
    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("19-ORDER-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("판매수량")
                .targetField("totalQuantity")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("판매총액")
                .targetField("totalPrice")
                .build());

        Map<String, String> orderedAtParams = Maps.newLinkedHashMap();
        orderedAtParams.put("판매일자", "java.lang.String");
        orderedAtParams.put("판매시간", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("hyundaiConversionService")
                .operatorMethod("getDateAndTime")
                .operatorParams(orderedAtParams)
                .targetField("orderedAt")
                .build());
        return list;
    }
}
