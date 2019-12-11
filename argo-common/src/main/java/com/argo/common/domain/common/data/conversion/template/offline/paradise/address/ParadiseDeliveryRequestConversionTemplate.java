package com.argo.common.domain.common.data.conversion.template.offline.paradise.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParadiseDeliveryRequestConversionTemplate {
    public static ConversionTemplate getDeliveryRequestTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("20-ORDER-DeliveryRequest")
                .targetId("com.argo.common.domain.order.DeliveryRequest")
                .rules(getConversionRuleForDeliveryRequest())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForDeliveryRequest() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("memo")
                .targetField("deliveryRequest")
                .build());

        return list;
    }
}
