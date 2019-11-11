package com.argo.common.domain.common.data.conversion.template.ezadmin.order;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EZAdminOrderMetadataConversionTemplate {
    public static ConversionTemplate getOrderMetadataTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("2-ORDER-OrderMetadata")
                .targetId("com.argo.common.domain.order.OrderMetadata")
                .rules(getConversionRuleForOrderMetadata())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderMetadata() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("total_qty")
                .targetField("totalQuantity")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.AGGREGATE)
                .sourceField("prd_shop_price")
                .targetField("totalPrice")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("collect_date")
                .targetField("collectedAt")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("order_date")
                .targetField("orderedAt")
                .build());
        return list;
    }
}
