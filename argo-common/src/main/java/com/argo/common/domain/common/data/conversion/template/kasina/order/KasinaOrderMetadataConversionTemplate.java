package com.argo.common.domain.common.data.conversion.template.kasina.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KasinaOrderMetadataConversionTemplate {
    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("15-ORDER-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("수량")
                .targetField("totalQuantity")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("판매가")
                .targetField("totalPrice")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("판매일자")
                .targetField("orderedAt")
                .build());
        return list;
    }
}
