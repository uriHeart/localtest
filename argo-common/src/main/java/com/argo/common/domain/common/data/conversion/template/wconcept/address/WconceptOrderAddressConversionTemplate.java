package com.argo.common.domain.common.data.conversion.template.wconcept.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WconceptOrderAddressConversionTemplate {
    public static ConversionTemplate getOrderAddressTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8")
                .targetId("com.argo.common.domain.order.OrderAddress")
                .rules(getConversionRuleForOrderAddress())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderAddress() {
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
                .sourceField("우편번호")
                .targetField("originalPostalCode")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("originalAddress")
                .conversionTemplateSourceId("8-OriginalAddress")
                .conversionTemplateTargetId("com.argo.common.domain.order.OriginalAddress")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("orderer")
                .conversionTemplateSourceId("8-Orderer")
                .conversionTemplateTargetId("com.argo.common.domain.order.Orderer")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("recipient")
                .conversionTemplateSourceId("8-Recipient")
                .conversionTemplateTargetId("com.argo.common.domain.order.Recipient")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("deliveryRequest")
                .conversionTemplateSourceId("8-DeliveryRequest")
                .conversionTemplateTargetId("com.argo.common.domain.order.DeliveryRequest")
                .build());

        return list;
    }
}