package com.argo.common.domain.common.data.conversion.template.wconcept.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WconceptOrderMetadataConversionTemplate {

    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("6-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .listReference("dataRows")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private WconceptOrderMetadataConversionTemplate(){
        throw new IllegalStateException("Utility class");
    }

    private static List<ConversionRule> getConversionRuleForOrderMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("수량")
                .targetField("totalQuantity")
                .build());

        Map<String, String> refundAmtParam = Maps.newLinkedHashMap();
        refundAmtParam .put("금액", "java.lang.String");
        list.add(ConversionRule.builder()
                        .conversionType(ConversionType.OPERATION)
                        .operatorClass("amountFormatConversionService")
                        .operatorMethod("replaceComa")
                        .operatorParams(refundAmtParam)
                        .targetField("totalPrice")
                        .build());

        Map<String, String> dateFormatParam = Maps.newLinkedHashMap();
        dateFormatParam .put("주민일자", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("dateFormatConversionService")
                .operatorMethod("addTime")
                .operatorParams(dateFormatParam)
                .targetField("orderedAt")
                .build());
        return list;
    }
}
