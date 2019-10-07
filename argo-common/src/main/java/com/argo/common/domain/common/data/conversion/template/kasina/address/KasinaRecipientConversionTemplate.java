package com.argo.common.domain.common.data.conversion.template.kasina.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KasinaRecipientConversionTemplate {
    public static ConversionTemplate getRecipientTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("15-ORDER-Recipient")
                .targetId("com.argo.common.domain.order.Recipient")
                .rules(getConversionRuleForRecipient())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForRecipient() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("받는분이름")
                .targetField("name")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("받는분전화번호")
                .targetField("phoneNumber1")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("받는분핸드폰")
                .targetField("phoneNumber2")
                .build());

        return list;
    }
}
