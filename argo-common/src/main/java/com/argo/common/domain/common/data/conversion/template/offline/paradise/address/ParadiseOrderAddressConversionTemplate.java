package com.argo.common.domain.common.data.conversion.template.offline.paradise.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ParadiseOrderAddressConversionTemplate {
    public static ConversionTemplate getOrderAddressTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("20-ORDER")
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
                .sourceField("recv_zip")
                .targetField("originalPostalCode")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("originalAddress")
                .conversionTemplateSourceId("20-ORDER-OriginalAddress")
                .conversionTemplateTargetId("com.argo.common.domain.order.OriginalAddress")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("orderer")
                .conversionTemplateSourceId("20-ORDER-Orderer")
                .conversionTemplateTargetId("com.argo.common.domain.order.Orderer")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("recipient")
                .conversionTemplateSourceId("20-ORDER-Recipient")
                .conversionTemplateTargetId("com.argo.common.domain.order.Recipient")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("deliveryRequest")
                .conversionTemplateSourceId("20-ORDER-DeliveryRequest")
                .conversionTemplateTargetId("com.argo.common.domain.order.DeliveryRequest")
                .build());

        return list;
    }
}