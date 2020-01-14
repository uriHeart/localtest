package com.argo.common.domain.common.data.conversion.template.player.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PlayerOrderMetadataConversionTemplate {

    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("1-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .listReference("dataRows")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private PlayerOrderMetadataConversionTemplate(){
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
                .sourceField("환불여부")
                .targetField("isRefund")
                .build());

        Map<String, String> refundAmtParam = Maps.newLinkedHashMap();
        refundAmtParam .put("환불금액", "java.lang.String");
        list.add(ConversionRule.builder()
                        .conversionType(ConversionType.OPERATION)
                        .operatorClass("amountFormatConversionService")
                        .operatorMethod("convertPlayerRefundAmtFormat")
                        .operatorParams(refundAmtParam)
                        .targetField("cancelPrice")
                        .build());

        Map<String, String> dateFormatParam = Maps.newLinkedHashMap();
        dateFormatParam .put("ord_date", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("dateFormatConversionService")
                .operatorMethod("convertDateFormat")
                .operatorParams(dateFormatParam)
                .targetField("orderedAt")
                .build());
        return list;
    }
}
