package com.argo.common.domain.common.data.conversion.template.wconcept.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WconceptRecipientConversionTemplate {
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
                .sourceField("수취인")
                .targetField("name")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("수취인연락처1")
                .targetField("phoneNumber1")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("수취인연락처2")
                .targetField("phoneNumber2")
                .build());

        return list;
    }
}
