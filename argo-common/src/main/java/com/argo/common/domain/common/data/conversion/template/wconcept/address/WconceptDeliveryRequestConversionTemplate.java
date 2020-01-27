package com.argo.common.domain.common.data.conversion.template.wconcept.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WconceptDeliveryRequestConversionTemplate {
    public static ConversionTemplate getDeliveryRequestTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("6-DeliveryRequest")
                .targetId("com.argo.common.domain.order.DeliveryRequest")
                .rules(getConversionRuleForDeliveryRequest())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForDeliveryRequest() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("배송메모")
                .targetField("deliveryRequest")
                .build());

        return list;
    }
}
