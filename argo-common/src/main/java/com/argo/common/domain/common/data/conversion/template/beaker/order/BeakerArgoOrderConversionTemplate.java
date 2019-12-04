package com.argo.common.domain.common.data.conversion.template.beaker.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BeakerArgoOrderConversionTemplate {
    public static ConversionTemplate getArgoOrderTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("17-ORDER")
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

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("출고일/회수일")
                .targetField("paidAt")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("주문/클레임상태")
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
                .conversionTemplateSourceId("17-ORDER-OrderMetadata")
                .conversionTemplateTargetId("com.argo.common.domain.order.OrderMetadata")
                .build());

        Map<String, String> paymentTypeParams = Maps.newLinkedHashMap();
        paymentTypeParams.put("결제방식", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("paymentTypeService")
                .operatorMethod("getPaymentType")
                .operatorParams(paymentTypeParams)
                .targetField("paymentType")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("실결제금액(A-B-H)")
                .targetField("totalAmount")
                .build());

        return list;
    }
}
