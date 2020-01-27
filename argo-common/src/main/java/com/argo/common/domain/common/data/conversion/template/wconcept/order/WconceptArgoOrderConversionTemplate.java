package com.argo.common.domain.common.data.conversion.template.wconcept.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WconceptArgoOrderConversionTemplate {

    public static ConversionTemplate getArgoOrderTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("6")
                .targetId("com.argo.common.domain.order.ArgoOrder")
                .rules(getConversionRuleForArgoOrder())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForArgoOrder() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("vendorId")
                .targetField("vendorId")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("channelId")
                .targetField("channelId")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("orderId")
                .targetField("orderId")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("publishedAt")
                .targetField("publishedAt")
                .build());

        Map<String, String> stateParam = Maps.newLinkedHashMap();
        stateParam .put("eventType", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("codeService")
                .operatorMethod("getOrderStatusMapping")
                .operatorParams(stateParam)
                .targetField("state")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("event")
                .targetField("event")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("metadata")
                .conversionTemplateSourceId("6-OrderMetadata")
                .conversionTemplateTargetId("com.argo.common.domain.order.OrderMetadata")
                .build());

        Map<String, String> amtParam = Maps.newLinkedHashMap();
        amtParam .put("판매가", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("amountFormatConversionService")
                .operatorMethod("replaceComa")
                .operatorParams(amtParam)
                .targetField("totalAmount")
                .build());

        return list;
    }
}
