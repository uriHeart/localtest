package com.argo.common.domain.common.data.conversion.template.musinsa.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusinsaOriginalAddressConversionTemplate {
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
                .sourceField("R_ADDR")
                .targetField("fullAddress")
                .build());

        return list;
    }
}
