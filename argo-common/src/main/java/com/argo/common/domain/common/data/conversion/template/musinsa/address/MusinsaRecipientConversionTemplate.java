package com.argo.common.domain.common.data.conversion.template.musinsa.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusinsaRecipientConversionTemplate {
    public static ConversionTemplate getRecipientTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8-Recipient")
                .targetId("com.argo.common.domain.order.Recipient")
                .rules(getConversionRuleForRecipient())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForRecipient() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("R_NM")
                .targetField("name")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("R_PHONE")
                .targetField("phoneNumber1")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("R_MOBILE")
                .targetField("phoneNumber2")
                .build());

        return list;
    }
}
