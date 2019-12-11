package com.argo.common.domain.common.data.conversion.template.ezadmin.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EZAdminOrderMetadataConversionTemplate {
    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("2-ORDER-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("total_qty")
                .targetField("totalQuantity")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("prd_shop_price")
                .targetField("totalPrice")
                .build());

        Map<String, String> orderedAtParams = Maps.newLinkedHashMap();
        orderedAtParams .put("order_date", "java.lang.String");
        orderedAtParams .put("order_time", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("EZAdminConversionService")
                .operatorMethod("mergeDateAndTime")
                .operatorParams(orderedAtParams)
                .targetField("orderedAt")
                .build());

        return list;
    }
}
