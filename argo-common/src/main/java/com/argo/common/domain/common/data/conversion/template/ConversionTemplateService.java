package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.ConvertibleData;
import com.argo.common.domain.common.data.conversion.template.ezadmin.address.*;
import com.argo.common.domain.common.data.conversion.template.ezadmin.order.EZAdminArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.order.EZAdminOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.vendoritem.EZAdminOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.vendoritem.EZAdminOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.address.*;
import com.argo.common.domain.common.data.conversion.template.kasina.order.KasinaArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.order.KasinaOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.vendoritem.KasinaOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.vendoritem.KasinaOrderVendorItemMetadataConversionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            // EZAdmin Template Mapping
            case "2-null-com.argo.common.domain.order.ArgoOrder":
                return getEZAdminArgoOrderTemplate();
            case "2-null-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getEZAdminArgoOrderMetadataTemplate();
            case "2-null-com.argo.common.domain.order.OrderAddress":
                return getEZAdminOrderAddressTemplate();
            case "2-null-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getEZAdminOriginalAddressTemplate();
            case "2-null-Orderer-com.argo.common.domain.order.Orderer":
                return getEZAdminOrdererTemplate();
            case "2-null-Recipient-com.argo.common.domain.order.Recipient":
                return getEZAdminRecipientTemplate();
            case "2-null-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getEZAdminDeliveryRequestTemplate();
            case "2-null-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getEZAdminOrderVendorItemLifecycleTemplate();
            case "2-null-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getEZAdminOrderVendorItemMetadataTemplate();
            // Kasina Template Mapping
            case "15-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getKasinaArgoOrderTemplate();
            case "15-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getKasinaArgoOrderMetadataTemplate();
            case "15-ORDER-com.argo.common.domain.order.OrderAddress":
                return getKasinaOrderAddressTemplate();
            case "15-ORDER-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getKasinaOriginalAddressTemplate();
            case "15-ORDER-Orderer-com.argo.common.domain.order.Orderer":
                return getKasinaOrdererTemplate();
            case "15-ORDER-Recipient-com.argo.common.domain.order.Recipient":
                return getKasinaRecipientTemplate();
            case "15-ORDER-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getKasinaDeliveryRequestTemplate();
            case "15-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getKasinaOrderVendorItemLifecycleTemplate();
            case "15-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getKasinaOrderVendorItemMetadataTemplate();
        }
        return null;
    }
    
    // EZAdmin Templates
    public ConversionTemplate getEZAdminArgoOrderTemplate() { return EZAdminArgoOrderConversionTemplate.getArgoOrderTemplate(); }

    public ConversionTemplate getEZAdminArgoOrderMetadataTemplate() { return EZAdminOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getEZAdminOrderAddressTemplate() { return EZAdminOrderAddressConversionTemplate.getOrderAddressTemplate(); }

    public ConversionTemplate getEZAdminOriginalAddressTemplate() { return EZAdminOriginalAddressConversionTemplate.getOriginalAddressTemplate(); }

    public ConversionTemplate getEZAdminOrdererTemplate() { return EZAdminOrdererConversionTemplate.getOrdererTemplate(); }

    public ConversionTemplate getEZAdminRecipientTemplate() { return EZAdminRecipientConversionTemplate.getRecipientTemplate(); }

    public ConversionTemplate getEZAdminDeliveryRequestTemplate() { return EZAdminDeliveryRequestConversionTemplate.getDeliveryRequestTemplate(); }

    public ConversionTemplate getEZAdminOrderVendorItemLifecycleTemplate() { return EZAdminOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getEZAdminOrderVendorItemMetadataTemplate() { return EZAdminOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }
    
    // Kasina Templates
    public ConversionTemplate getKasinaArgoOrderTemplate() { return KasinaArgoOrderConversionTemplate.getArgoOrderTemplate(); }

    public ConversionTemplate getKasinaArgoOrderMetadataTemplate() { return KasinaOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getKasinaOrderAddressTemplate() { return KasinaOrderAddressConversionTemplate.getOrderAddressTemplate(); }

    public ConversionTemplate getKasinaOriginalAddressTemplate() { return KasinaOriginalAddressConversionTemplate.getOriginalAddressTemplate(); }

    public ConversionTemplate getKasinaOrdererTemplate() { return KasinaOrdererConversionTemplate.getOrdererTemplate(); }

    public ConversionTemplate getKasinaRecipientTemplate() { return KasinaRecipientConversionTemplate.getRecipientTemplate(); }

    public ConversionTemplate getKasinaDeliveryRequestTemplate() { return KasinaDeliveryRequestConversionTemplate.getDeliveryRequestTemplate(); }

    public ConversionTemplate getKasinaOrderVendorItemLifecycleTemplate() { return KasinaOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getKasinaOrderVendorItemMetadataTemplate() { return KasinaOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }

    public Map<String, ConversionTemplate> getRawEventConversionTemplateMap(ConvertibleData convertibleData) {
        Map templateMap = new HashMap<String, ConversionTemplate>();

        ConversionTemplate argoOrderTemplate = null;
        ConversionTemplate orderAddressTemplate = null;
        ConversionTemplate vendorItemLifecycleTemplate = null;

        switch (convertibleData.sourceKey()) {
            case "2-null":
                argoOrderTemplate = getEZAdminArgoOrderTemplate();
                orderAddressTemplate = getEZAdminOrderAddressTemplate();
                vendorItemLifecycleTemplate = getEZAdminOrderVendorItemLifecycleTemplate();
    
                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;
            case "15-ORDER":
                argoOrderTemplate = getKasinaArgoOrderTemplate();
                orderAddressTemplate = getKasinaOrderAddressTemplate();
                vendorItemLifecycleTemplate = getKasinaOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;
        }
        return templateMap;

    }
}
