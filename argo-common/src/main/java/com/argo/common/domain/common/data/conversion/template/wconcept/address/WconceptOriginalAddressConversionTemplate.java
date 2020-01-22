package com.argo.common.domain.common.data.conversion.template.wconcept.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WconceptOriginalAddressConversionTemplate {
    public static ConversionTemplate getOriginalAddressTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8-OriginalAddress")
                .targetId("com.argo.common.domain.order.OriginalAddress")
                .rules(getConversionRuleForOriginalAddress())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOriginalAddress() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("배송지")
                .targetField("fullAddress")
                .build());

        return list;
    }
}
