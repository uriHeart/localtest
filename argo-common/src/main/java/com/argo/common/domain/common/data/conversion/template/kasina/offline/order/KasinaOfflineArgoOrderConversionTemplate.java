package com.argo.common.domain.common.data.conversion.template.kasina.offline.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KasinaOfflineArgoOrderConversionTemplate {
    public static ConversionTemplate getArgoOrderTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("16-ORDER")
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
                .sourceField("판매일자")
                .targetField("paidAt")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CUSTOM_INPUT)
                .customType("java.lang.String")
                .customValue("ORDER_COMPLETE")
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
                .conversionTemplateSourceId("16-ORDER-OrderMetadata")
                .conversionTemplateTargetId("com.argo.common.domain.order.OrderMetadata")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CUSTOM_INPUT)
                .customType("java.lang.String")
                .customValue("CREDITCARD")
                .targetField("paymentType")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("판매가")
                .targetField("totalAmount")
                .build());

        return list;
    }
}
