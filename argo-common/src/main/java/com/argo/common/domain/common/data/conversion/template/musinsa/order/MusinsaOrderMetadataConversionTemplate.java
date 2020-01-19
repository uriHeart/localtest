package com.argo.common.domain.common.data.conversion.template.musinsa.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MusinsaOrderMetadataConversionTemplate {

    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .listReference("dataRows")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private MusinsaOrderMetadataConversionTemplate(){
        throw new IllegalStateException("Utility class");
    }

    private static List<ConversionRule> getConversionRuleForOrderMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("qty")
                .targetField("totalQuantity")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("price")
                .targetField("totalPrice")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("REFUND_YN")
                .targetField("isRefund")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("REFUND_AMT")
                .targetField("cancelPrice")
                .build());


        Map<String, String> dateFormatParam = Maps.newLinkedHashMap();
        dateFormatParam .put("ord_date", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("dateFormatConversionService")
                .operatorMethod("convertDateFormatUTC")
                .operatorParams(dateFormatParam)
                .targetField("orderedAt")
                .build());
        return list;
    }
}
