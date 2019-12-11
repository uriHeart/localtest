package com.argo.common.domain.common.data.conversion.template.ezadmin.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EZAdminArgoOrderConversionTemplate {
    public static ConversionTemplate getArgoOrderTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("2-ORDER")
                .targetId("com.argo.common.domain.order.ArgoOrder")
                .rules(getConversiontRuleForArgoOrder())
                .build();
    }

    private static List<ConversionRule> getConversiontRuleForArgoOrder() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("vendorId")
                .targetField("vendorId")
                .build());

        Map<String, String> channelIdParams = Maps.newLinkedHashMap();
        channelIdParams.put("vendorId", "java.lang.Long");
        channelIdParams.put("shop_id", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("vendorService")
                .operatorMethod("getChannelId")
                .operatorParams(channelIdParams)
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

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("order_date")
                .targetField("paidAt")
                .build());

        Map<String, String> stateParams = Maps.newLinkedHashMap();
        stateParams.put("status", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("codeService")
                .operatorMethod("getOrderStatusMapping")
                .operatorParams(stateParams)
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
                .conversionTemplateSourceId("2-ORDER-OrderMetadata")
                .conversionTemplateTargetId("com.argo.common.domain.order.OrderMetadata")
                .build());

        Map<String, String> paymentTypeParams = Maps.newLinkedHashMap();
        paymentTypeParams.put("pay_type", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("paymentTypeService")
                .operatorMethod("getPaymentType")
                .operatorParams(paymentTypeParams)
                .targetField("paymentType")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("amount")
                .targetField("totalAmount")
                .build());

        return list;
    }
}
