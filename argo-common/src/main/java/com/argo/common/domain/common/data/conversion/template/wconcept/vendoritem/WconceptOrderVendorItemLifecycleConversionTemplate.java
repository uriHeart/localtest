package com.argo.common.domain.common.data.conversion.template.wconcept.vendoritem;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WconceptOrderVendorItemLifecycleConversionTemplate {
    public static ConversionTemplate getOrderVendorItemLifecycleTemplate() {
        return ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("8")
                .targetId("com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle")
                .listReference("dataRows")
                .rules(getConversionRuleForOrderVendorItemLifecycle())
                .build();
    }

    private static List<ConversionRule> getConversionRuleForOrderVendorItemLifecycle() {
        List<ConversionRule> list = new ArrayList<>();

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("vendorId")
                .targetField("vendorId")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("channelId")
                .targetField("channelId")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("orderId")
                .targetField("orderId")
                .build());

        Map<String, String> vendorItemIdParams = Maps.newLinkedHashMap();
        vendorItemIdParams.put("vendorId", "java.lang.Long");
        vendorItemIdParams.put("아이템코드", "java.lang.String");
        vendorItemIdParams.put("상품명", "java.lang.String");
        vendorItemIdParams.put("옵션1", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("externalVendorItemMappingService")
                .operatorMethod("getVendorItem")
                .operatorParams(vendorItemIdParams)
                .targetField("vendorItemId")
                .build());

        Map<String, String> skuMappingParams = Maps.newLinkedHashMap();
        skuMappingParams.put("channelId", "java.lang.Long");
        skuMappingParams.put("vendorId", "java.lang.Long");
        skuMappingParams.put("goods_no", "java.lang.String");
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.OPERATION)
                .operatorClass("skuMappingProvider")
                .operatorMethod("getSkuIds")
                .operatorParams(skuMappingParams)
                .targetField("skuMappings")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("publishedAt")
                .targetField("publishedAt")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("아이템코드")
                .targetField("sourceItemId")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("상품명")
                .targetField("sourceItemName")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("옵션1")
                .targetField("sourceItemOption")
                .build());


        list.add(ConversionRule.builder()
                .conversionType(ConversionType.CONVERSION_TEMPLATE)
                .targetField("metadata")
                .conversionTemplateSourceId("8-OrderVendorItemMetadata")
                .conversionTemplateTargetId("com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata")
                .build());

        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("수량")
                .targetField("quantity")
                .build());

        return list;
    }
}
