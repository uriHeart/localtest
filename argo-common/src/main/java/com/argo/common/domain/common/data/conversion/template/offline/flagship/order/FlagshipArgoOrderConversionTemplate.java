package com.argo.common.domain.common.data.conversion.template.offline.flagship.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FlagshipArgoOrderConversionTemplate {
    public static ConversionTemplate getArgoOrderTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("18-ORDER")
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

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("createdAt")
                .targetField("createdAt")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("event")
                .targetField("event")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("metadata")
                .conversionTemplateSourceId("18-ORDER-OrderMetadata")
                .conversionTemplateTargetId("com.argo.common.domain.order.OrderMetadata")
                .build());

        Map<String, String> paymentTypeParams = Maps.newLinkedHashMap();
        paymentTypeParams.put("카드", "java.lang.Long");
        paymentTypeParams.put("현금", "java.lang.Long");
        paymentTypeParams.put("수표", "java.lang.Long");
        paymentTypeParams.put("상품권", "java.lang.Long");
        paymentTypeParams.put("외상", "java.lang.Long");
        paymentTypeParams.put("포인트", "java.lang.Long");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("flagshipConversionService")
                .operatorMethod("getPaymentType")
                .operatorParams(paymentTypeParams)
                .targetField("paymentType")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("메뉴별 판매가")
                .targetField("totalAmount")
                .build());

        return list;
    }
}
