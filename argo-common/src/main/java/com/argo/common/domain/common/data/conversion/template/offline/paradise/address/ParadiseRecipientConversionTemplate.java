package com.argo.common.domain.common.data.conversion.template.offline.paradise.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParadiseRecipientConversionTemplate {
    public static ConversionTemplate getRecipientTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("20-ORDER-Recipient")
                .targetId("com.argo.common.domain.order.Recipient")
                .rules(getConversionRuleForRecipient())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForRecipient() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("recv_name")
                .targetField("name")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("recv_tel1")
                .targetField("phoneNumber1")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("recv_tel2")
                .targetField("phoneNumber2")
                .build());

        return list;
    }
}
