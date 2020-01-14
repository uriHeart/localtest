package com.argo.common.domain.common.data.conversion.template.musinsa.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusinsaOrdererConversionTemplate {
    public static ConversionTemplate getOrdererTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8-Orderer")
                .targetId("com.argo.common.domain.order.Orderer")
                .rules(getConversionRuleForOrderer())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderer() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("USER_NM")
                .targetField("name")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("PHONE")
                .targetField("phoneNumber1")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("MOBILE")
                .targetField("phoneNumber2")
                .build());

        return list;
    }
}
