package com.argo.common.domain.common.data.conversion.template.wconcept.address;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WconceptOrdererConversionTemplate {
    public static ConversionTemplate getOrdererTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("6-Orderer")
                .targetId("com.argo.common.domain.order.Orderer")
                .rules(getConversionRuleForOrderer())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderer() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("주문자")
                .targetField("name")
                .build());

//        list.add(ConversionRule.builder()
//                .conversionType(ConversionType.DIRECT)
//                .sourceField("PHONE")
//                .targetField("phoneNumber1")
//                .build());
//
//        list.add(ConversionRule.builder()
//                .conversionType(ConversionType.DIRECT)
//                .sourceField("MOBILE")
//                .targetField("phoneNumber2")
//                .build());

        return list;
    }
}
