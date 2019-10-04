package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.ConvertibleData;
import com.argo.common.domain.order.ArgoOrder;
import com.argo.common.domain.raw.RawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConversionTemplateService {
    @Autowired
    private ConversionTemplateRepository conversionTemplateRepository;

    public Map<String, ConversionTemplate> getConversionTemplateMaps(ConvertibleData convertibleData) {
        return getConversionTemplateMaps(convertibleData.sourceKey());
    }

    public Map<String, ConversionTemplate> getConversionTemplateMaps(String sourceId) {
        List<ConversionTemplate> templates = conversionTemplateRepository.findAllBySourceId(sourceId);
        Map<String, ConversionTemplate> map = templates
                .stream()
                .collect(Collectors.toMap(ConversionTemplate::getTargetId, Function.identity()));
        return map;
    }

    public void save(ConversionTemplate conversionTemplate) {
        conversionTemplateRepository.save(conversionTemplate);
    }

    public Map<String, ConversionTemplate> getTestTemplate(ConvertibleData convertibleData) {
        List<ConversionRule> ruleList = new ArrayList<>();

        ConversionTemplate template = ConversionTemplate.builder()
                .createdAt(new Date())
                .expiredAt(null)
                .sourceId("2-null")
                .targetId("ArgoOrder")
                .rules(getTestRuleForArgoOrder())
                .build();
        HashMap map = new HashMap();

        map.put("2-null", template);
        return map;
    }

    private List<ConversionRule> getTestRuleForArgoOrder() {
        List<ConversionRule> list = new ArrayList<>();
        ArgoOrder order;
        RawEvent event;

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
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("publishedAt")
                .targetField("publishedAt")
                .build());
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("order_date")
                .targetField("paidAt")
                .build());
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("data")
                .targetField("metadata")
                .build());
        list.add(ConversionRule.builder()
                .conversionType(ConversionType.DIRECT)
                .sourceField("status")
                .targetField("state")
                .build());

        return list;
    }
}
