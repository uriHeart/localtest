package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.ConvertibleData;
import com.argo.common.domain.common.data.conversion.template.address.*;
import com.argo.common.domain.common.data.conversion.template.order.ArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.order.OrderMetadataConversionTemplate;
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

    public ConversionTemplate getConversionTemplate(String sourceId, String targetId) {
        return conversionTemplateRepository.findFirstBySourceIdAndTargetIdOrderByExpiredAtDesc(sourceId, targetId);
    }

    public void save(ConversionTemplate conversionTemplate) {
        conversionTemplateRepository.save(conversionTemplate);
    }


    public ConversionTemplate conversionTemplateBySourceIdAndTargetId(String sourceId, String targetId) {
        switch (sourceId + "-" + targetId) {
            case "2-null-com.argo.common.domain.order.ArgoOrder":
                return getArgoOrderTemplate();
            case "2-null-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getArgoOrderMetadataTemplate();
            case "2-null-com.argo.common.domain.order.OrderAddress":
                return getOrderAddressTemplate();
            case "2-null-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getOriginalAddressTemplate();
            case "2-null-Orderer-com.argo.common.domain.order.Orderer":
                return getOrdererTemplate();
            case "2-null-Recipient-com.argo.common.domain.order.Recipient":
                return getRecipientTemplate();
            case "2-null-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getDeliveryRequestTemplate();
        }
        return null;
    }

    public ConversionTemplate getArgoOrderTemplate() {
        return ArgoOrderConversionTemplate.getArgoOrderTemplate();
    }

    public ConversionTemplate getArgoOrderMetadataTemplate() {
        return OrderMetadataConversionTemplate.getOrderMetadataTemplate();
    }

    public ConversionTemplate getOrderAddressTemplate() {
        return OrderAddressConversionTemplate.getOrderAddressTemplate();
    }

    public ConversionTemplate getOriginalAddressTemplate() {
        return OriginalAddressConversionTemplate.getOriginalAddressTemplate();
    }

    public ConversionTemplate getOrdererTemplate() {
        return OrdererConversionTemplate.getOrdererTemplate();
    }

    public ConversionTemplate getRecipientTemplate() {
        return RecipientConversionTemplate.getRecipientTemplate();
    }

    public ConversionTemplate getDeliveryRequestTemplate() {
        return DeliveryRequestConversionTemplate.getDeliveryRequestTemplate();
    }

    public Map<String, ConversionTemplate> getRawEventConversionTemplateMap(ConvertibleData convertibleData) {
        Map templateMap = new HashMap<String, ConversionTemplate>();

        ConversionTemplate argoOrderTemplate = getArgoOrderTemplate();
        ConversionTemplate orderAddressTemplate = getOrderAddressTemplate();

        templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
        templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);

        return templateMap;

    }
}
