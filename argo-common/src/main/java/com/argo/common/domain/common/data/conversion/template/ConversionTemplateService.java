package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.SourceData;
import com.argo.common.domain.common.data.conversion.template.beaker.order.BeakerArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.beaker.order.BeakerOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.beaker.vendoritem.BeakerOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.beaker.vendoritem.BeakerOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.address.*;
import com.argo.common.domain.common.data.conversion.template.ezadmin.order.EZAdminArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.order.EZAdminOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.vendoritem.EZAdminOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ezadmin.vendoritem.EZAdminOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.offline.order.KasinaOfflineArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.offline.order.KasinaOfflineOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.offline.vendoritem.KasinaOfflineOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.offline.vendoritem.KasinaOfflineOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.online.address.*;
import com.argo.common.domain.common.data.conversion.template.kasina.online.order.KasinaOnlineArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.online.order.KasinaOnlineOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.online.vendoritem.KasinaOnlineOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.kasina.online.vendoritem.KasinaOnlineOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.musinsa.address.*;
import com.argo.common.domain.common.data.conversion.template.musinsa.order.MusinsaArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.musinsa.order.MusinsaOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.musinsa.vendoritem.MusinsaOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.musinsa.vendoritem.MusinsaOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.flagship.order.FlagshipArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.flagship.order.FlagshipOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.flagship.vendoritem.FlagshipOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.flagship.vendoritem.FlagshipOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.hyundai.order.HyundaiArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.hyundai.order.HyundaiOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.hyundai.vendoritem.HyundaiOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.offline.hyundai.vendoritem.HyundaiOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.player.address.*;
import com.argo.common.domain.common.data.conversion.template.player.order.PlayerArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.player.order.PlayerOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.player.vendorItem.PlayerOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.player.vendorItem.PlayerOrderVendorItemMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.wconcept.address.*;
import com.argo.common.domain.common.data.conversion.template.wconcept.order.WconceptArgoOrderConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.wconcept.order.WconceptOrderMetadataConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.wconcept.vendoritem.WconceptOrderVendorItemLifecycleConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.wconcept.vendoritem.WconceptOrderVendorItemMetadataConversionTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConversionTemplateService {
    @Autowired
    private ConversionTemplateRepository conversionTemplateRepository;

    //@Autowired
    //private ReactiveConversionTemplateRepository reactiveConversionTemplateRepository;

    public void save(ConversionTemplate conversionTemplate) {
        if (conversionTemplate == null) {
            conversionTemplateRepository.save(conversionTemplate);
        }
    }

    public Map<String, ConversionTemplate> getConversionTemplateMaps(SourceData sourceData) {
        return getConversionTemplateMaps(sourceData.sourceKey());
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

    public ConversionTemplate conversionTemplateBySourceIdAndTargetId(String sourceId, String targetId) {
        switch (sourceId + "-" + targetId) {
            // EZAdmin Template Mapping
            case "2-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getEZAdminArgoOrderTemplate();
            case "2-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getEZAdminArgoOrderMetadataTemplate();
            case "2-ORDER-com.argo.common.domain.order.OrderAddress":
                return getEZAdminOrderAddressTemplate();
            case "2-ORDER-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getEZAdminOriginalAddressTemplate();
            case "2-ORDER-Orderer-com.argo.common.domain.order.Orderer":
                return getEZAdminOrdererTemplate();
            case "2-ORDER-Recipient-com.argo.common.domain.order.Recipient":
                return getEZAdminRecipientTemplate();
            case "2-ORDER-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getEZAdminDeliveryRequestTemplate();
            case "2-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getEZAdminOrderVendorItemLifecycleTemplate();
            case "2-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getEZAdminOrderVendorItemMetadataTemplate();

            // KasinaOnline Template Mapping
            case "15-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getKasinaOnlineArgoOrderTemplate();
            case "15-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getKasinaOnlineArgoOrderMetadataTemplate();
            case "15-ORDER-com.argo.common.domain.order.OrderAddress":
                return getKasinaOnlineOrderAddressTemplate();
            case "15-ORDER-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getKasinaOnlineOriginalAddressTemplate();
            case "15-ORDER-Orderer-com.argo.common.domain.order.Orderer":
                return getKasinaOnlineOrdererTemplate();
            case "15-ORDER-Recipient-com.argo.common.domain.order.Recipient":
                return getKasinaOnlineRecipientTemplate();
            case "15-ORDER-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getKasinaOnlineDeliveryRequestTemplate();
            case "15-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getKasinaOnlineOrderVendorItemLifecycleTemplate();
            case "15-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getKasinaOnlineOrderVendorItemMetadataTemplate();

            // KasinaOffline Template Mapping
            case "16-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getKasinaOfflineArgoOrderTemplate();
            case "16-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getKasinaOfflineArgoOrderMetadataTemplate();
            case "16-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getKasinaOfflineOrderVendorItemLifecycleTemplate();
            case "16-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getKasinaOfflineOrderVendorItemMetadataTemplate();

            // Beaker Template Mapping
            case "17-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getBeakerArgoOrderTemplate();
            case "17-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getBeakerArgoOrderMetadataTemplate();
            case "17-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getBeakerOrderVendorItemLifecycleTemplate();
            case "17-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getBeakerOrderVendorItemMetadataTemplate();

            // Flagship Template Mapping
            case "18-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getFlagshipArgoOrderTemplate();
            case "18-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getFlagshipArgoOrderMetadataTemplate();
            case "18-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getFlagshipOrderVendorItemLifecycleTemplate();
            case "18-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getFlagshipOrderVendorItemMetadataTemplate();

            // Hyundai Template Mapping
            case "19-ORDER-com.argo.common.domain.order.ArgoOrder":
                return getHyundaiArgoOrderTemplate();
            case "19-ORDER-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getHyundaiArgoOrderMetadataTemplate();
            case "19-ORDER-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getHyundaiOrderVendorItemLifecycleTemplate();
            case "19-ORDER-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getHyundaiOrderVendorItemMetadataTemplate();

            //Wconcept
            case "6-com.argo.common.domain.order.ArgoOrder":
                return getWconceptArgoOrderTemplate();
            case "6-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getWconceptArgoOrderMetadataTemplate();
            case "6-com.argo.common.domain.order.OrderAddress":
                return getWconceptOrderAddressTemplate();
            case "6-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getWconceptOriginalAddressTemplate();
            case "6-Orderer-com.argo.common.domain.order.Orderer":
                return getWconceptOrdererTemplate();
            case "6-Recipient-com.argo.common.domain.order.Recipient":
                return getWconceptRecipientTemplate();
            case "6-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getWconceptDeliveryRequestTemplate();
            case "6-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getWconceptOrderVendorItemLifecycleTemplate();
            case "6-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getWconceptOrderVendorItemMetadataTemplate();

            //musinsa
            case "8-com.argo.common.domain.order.ArgoOrder":
                return getMusinsaArgoOrderTemplate();
            case "8-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getMusinsaArgoOrderMetadataTemplate();
            case "8-com.argo.common.domain.order.OrderAddress":
                return getMusinsaOrderAddressTemplate();
            case "8-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getMusinsaOriginalAddressTemplate();
            case "8-Orderer-com.argo.common.domain.order.Orderer":
                return getMusinsaOrdererTemplate();
            case "8-Recipient-com.argo.common.domain.order.Recipient":
                return getMusinsaRecipientTemplate();
            case "8-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getMusinsaDeliveryRequestTemplate();
            case "8-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getMusinsaOrderVendorItemLifecycleTemplate();
            case "8-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getMusinsaOrderVendorItemMetadataTemplate();

            //player
            case "1-com.argo.common.domain.order.ArgoOrder":
                return getPlayerArgoOrderTemplate();
            case "1-OrderMetadata-com.argo.common.domain.order.OrderMetadata":
                return getPlayerArgoOrderMetadataTemplate();
            case "1-com.argo.common.domain.order.OrderAddress":
                return getPlayerOrderAddressTemplate();
            case "1-OriginalAddress-com.argo.common.domain.order.OriginalAddress":
                return getPlayerOriginalAddressTemplate();
            case "1-Orderer-com.argo.common.domain.order.Orderer":
                return getPlayerOrdererTemplate();
            case "1-Recipient-com.argo.common.domain.order.Recipient":
                return getPlayerRecipientTemplate();
            case "1-DeliveryRequest-com.argo.common.domain.order.DeliveryRequest":
                return getPlayerDeliveryRequestTemplate();
            case "1-com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle":
                return getPlayerOrderVendorItemLifecycleTemplate();
            case "1-OrderVendorItemMetadata-com.argo.common.domain.order.vendoritem.OrderVendorItemMetadata":
                return getPlayerOrderVendorItemMetadataTemplate();
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

    // KasinaOnline Templates
    public ConversionTemplate getKasinaOnlineArgoOrderTemplate() { return KasinaOnlineArgoOrderConversionTemplate.getArgoOrderTemplate(); }

    public ConversionTemplate getKasinaOnlineArgoOrderMetadataTemplate() { return KasinaOnlineOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getKasinaOnlineOrderAddressTemplate() { return KasinaOnlineOrderAddressConversionTemplate.getOrderAddressTemplate(); }

    public ConversionTemplate getKasinaOnlineOriginalAddressTemplate() { return KasinaOnlineOriginalAddressConversionTemplate.getOriginalAddressTemplate(); }

    public ConversionTemplate getKasinaOnlineOrdererTemplate() { return KasinaOnlineOrdererConversionTemplate.getOrdererTemplate(); }

    public ConversionTemplate getKasinaOnlineRecipientTemplate() { return KasinaOnlineRecipientConversionTemplate.getRecipientTemplate(); }

    public ConversionTemplate getKasinaOnlineDeliveryRequestTemplate() { return KasinaOnlineDeliveryRequestConversionTemplate.getDeliveryRequestTemplate(); }

    public ConversionTemplate getKasinaOnlineOrderVendorItemLifecycleTemplate() { return KasinaOnlineOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getKasinaOnlineOrderVendorItemMetadataTemplate() { return KasinaOnlineOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }

    // KasinaOffline Templates
    public ConversionTemplate getKasinaOfflineArgoOrderTemplate() { return KasinaOfflineArgoOrderConversionTemplate.getArgoOrderTemplate(); }

    public ConversionTemplate getKasinaOfflineArgoOrderMetadataTemplate() { return KasinaOfflineOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getKasinaOfflineOrderVendorItemLifecycleTemplate() { return KasinaOfflineOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getKasinaOfflineOrderVendorItemMetadataTemplate() { return KasinaOfflineOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }

    // Beaker Templates
    public ConversionTemplate getBeakerArgoOrderTemplate() {return BeakerArgoOrderConversionTemplate.getArgoOrderTemplate();}

    public ConversionTemplate getBeakerArgoOrderMetadataTemplate() {return BeakerOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getBeakerOrderVendorItemLifecycleTemplate(){ return BeakerOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getBeakerOrderVendorItemMetadataTemplate(){ return BeakerOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }

    // Flagship Templates
    public ConversionTemplate getFlagshipArgoOrderTemplate() {return FlagshipArgoOrderConversionTemplate.getArgoOrderTemplate();}

    public ConversionTemplate getFlagshipArgoOrderMetadataTemplate() {return FlagshipOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getFlagshipOrderVendorItemLifecycleTemplate(){ return FlagshipOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getFlagshipOrderVendorItemMetadataTemplate(){ return FlagshipOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }

    // Hyundai Templates
    public ConversionTemplate getHyundaiArgoOrderTemplate() {return HyundaiArgoOrderConversionTemplate.getArgoOrderTemplate();}

    public ConversionTemplate getHyundaiArgoOrderMetadataTemplate() {return HyundaiOrderMetadataConversionTemplate.getOrderMetadataTemplate(); }

    public ConversionTemplate getHyundaiOrderVendorItemLifecycleTemplate(){ return HyundaiOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }

    public ConversionTemplate getHyundaiOrderVendorItemMetadataTemplate(){ return HyundaiOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }

    //Wconcept
    public ConversionTemplate getWconceptArgoOrderTemplate(){return WconceptArgoOrderConversionTemplate.getArgoOrderTemplate();}
    public ConversionTemplate getWconceptArgoOrderMetadataTemplate(){return WconceptOrderMetadataConversionTemplate.getOrderMetadataTemplate();}
    public ConversionTemplate getWconceptOrderVendorItemLifecycleTemplate(){ return WconceptOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }
    public ConversionTemplate getWconceptOrderVendorItemMetadataTemplate(){ return WconceptOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }
    public ConversionTemplate getWconceptOrderAddressTemplate() { return WconceptOrderAddressConversionTemplate.getOrderAddressTemplate(); }
    public ConversionTemplate getWconceptOriginalAddressTemplate() { return WconceptOriginalAddressConversionTemplate.getOriginalAddressTemplate(); }
    public ConversionTemplate getWconceptOrdererTemplate() { return WconceptOrdererConversionTemplate.getOrdererTemplate(); }
    public ConversionTemplate getWconceptRecipientTemplate() { return WconceptRecipientConversionTemplate.getRecipientTemplate(); }
    public ConversionTemplate getWconceptDeliveryRequestTemplate() { return WconceptDeliveryRequestConversionTemplate.getDeliveryRequestTemplate(); }

    //MUSINSA Templates
    public ConversionTemplate getMusinsaArgoOrderTemplate(){return MusinsaArgoOrderConversionTemplate.getArgoOrderTemplate();}
    public ConversionTemplate getMusinsaArgoOrderMetadataTemplate(){return MusinsaOrderMetadataConversionTemplate.getOrderMetadataTemplate();}
    public ConversionTemplate getMusinsaOrderVendorItemLifecycleTemplate(){ return MusinsaOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }
    public ConversionTemplate getMusinsaOrderVendorItemMetadataTemplate(){ return MusinsaOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }
    public ConversionTemplate getMusinsaOrderAddressTemplate() { return MusinsaOrderAddressConversionTemplate.getOrderAddressTemplate(); }
    public ConversionTemplate getMusinsaOriginalAddressTemplate() { return MusinsaOriginalAddressConversionTemplate.getOriginalAddressTemplate(); }
    public ConversionTemplate getMusinsaOrdererTemplate() { return MusinsaOrdererConversionTemplate.getOrdererTemplate(); }
    public ConversionTemplate getMusinsaRecipientTemplate() { return MusinsaRecipientConversionTemplate.getRecipientTemplate(); }
    public ConversionTemplate getMusinsaDeliveryRequestTemplate() { return MusinsaDeliveryRequestConversionTemplate.getDeliveryRequestTemplate(); }

    //Player
    public ConversionTemplate getPlayerArgoOrderTemplate(){return PlayerArgoOrderConversionTemplate.getArgoOrderTemplate();}
    public ConversionTemplate getPlayerArgoOrderMetadataTemplate(){return PlayerOrderMetadataConversionTemplate.getOrderMetadataTemplate();}
    public ConversionTemplate getPlayerOrderVendorItemLifecycleTemplate(){ return PlayerOrderVendorItemLifecycleConversionTemplate.getOrderVendorItemLifecycleTemplate(); }
    public ConversionTemplate getPlayerOrderVendorItemMetadataTemplate(){ return PlayerOrderVendorItemMetadataConversionTemplate.getOrderVendorItemMetadataTemplate(); }
    public ConversionTemplate getPlayerOrderAddressTemplate() { return PlayerOrderAddressConversionTemplate.getOrderAddressTemplate(); }
    public ConversionTemplate getPlayerOriginalAddressTemplate() { return PlayerOriginalAddressConversionTemplate.getOriginalAddressTemplate(); }
    public ConversionTemplate getPlayerOrdererTemplate() { return PlayerOrdererConversionTemplate.getOrdererTemplate(); }
    public ConversionTemplate getPlayerRecipientTemplate() { return PlayerRecipientConversionTemplate.getRecipientTemplate(); }
    public ConversionTemplate getPlayerDeliveryRequestTemplate() { return PlayerDeliveryRequestConversionTemplate.getDeliveryRequestTemplate(); }


    public Map<String, ConversionTemplate> getRawEventConversionTemplateMap(SourceData sourceData) {
        Map templateMap = new HashMap<String, ConversionTemplate>();

        ConversionTemplate argoOrderTemplate = null;
        ConversionTemplate orderAddressTemplate = null;
        ConversionTemplate vendorItemLifecycleTemplate = null;

        switch (sourceData.sourceKey()) {
            case "2-ORDER":
                argoOrderTemplate = getEZAdminArgoOrderTemplate();
                orderAddressTemplate = getEZAdminOrderAddressTemplate();
                vendorItemLifecycleTemplate = getEZAdminOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;
            case "15-ORDER":
                argoOrderTemplate = getKasinaOnlineArgoOrderTemplate();
                orderAddressTemplate = getKasinaOnlineOrderAddressTemplate();
                vendorItemLifecycleTemplate = getKasinaOnlineOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;

            case "16-ORDER":
                argoOrderTemplate = getKasinaOfflineArgoOrderTemplate();
                vendorItemLifecycleTemplate = getKasinaOfflineOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;

            case "17-ORDER":
                argoOrderTemplate = getBeakerArgoOrderTemplate();
                vendorItemLifecycleTemplate = getBeakerOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;

            case "18-ORDER":
                argoOrderTemplate = getFlagshipArgoOrderTemplate();
                vendorItemLifecycleTemplate = getFlagshipOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;

            case "19-ORDER":
                argoOrderTemplate = getHyundaiArgoOrderTemplate();
                vendorItemLifecycleTemplate = getHyundaiOrderVendorItemLifecycleTemplate();

                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                break;



            case "6-ORDER":
            case "6-CANCEL":
            case "6-PAYMENT_COMPLETE":
            case "6-RELEASE_REQUEST":
            case "6-DELIVERY_COMPLETE":
            case "6-EXCHANGE_REQUEST":
            case "6-EXCHANGE_PROCESS":
            case "6-EXCHANGE":
            case "6-RETURN_REQUEST":
            case "6-RETURN":
            case "6-RETURN_PROCESS":
            case "6-CLAIM_CANCEL":
            case "6-OTHER":
                argoOrderTemplate = getWconceptArgoOrderTemplate();
                vendorItemLifecycleTemplate = getWconceptOrderVendorItemLifecycleTemplate();
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                break;

            case "6-RELEASE":
            case "6-DELIVERY":
                argoOrderTemplate = getWconceptArgoOrderTemplate();
                vendorItemLifecycleTemplate = getWconceptOrderVendorItemLifecycleTemplate();
                orderAddressTemplate = getWconceptOrderAddressTemplate();
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);

                break;


            case "8-ORDER":
            case "8-CANCEL":
            case "8-PAYMENT_COMPLETE":
            case "8-RELEASE_REQUEST":
            case "8-RELEASE":
            case "8-DELIVERY":
            case "8-DELIVERY_COMPLETE":
            case "8-EXCHANGE_REQUEST":
            case "8-EXCHANGE_PROCESS":
            case "8-EXCHANGE":
            case "8-RETURN_REQUEST":
            case "8-RETURN":
            case "8-RETURN_PROCESS":
            case "8-CLAIM_CANCEL":
            case "8-OTHER":
                argoOrderTemplate = getMusinsaArgoOrderTemplate();
                orderAddressTemplate = getMusinsaOrderAddressTemplate();
                vendorItemLifecycleTemplate = getMusinsaOrderVendorItemLifecycleTemplate();
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);
                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                break;

            case "1-ORDER":
            case "1-CANCEL":
            case "1-PAYMENT_COMPLETE":
            case "1-RELEASE_REQUEST":
            case "1-RELEASE":
            case "1-DELIVERY":
            case "1-DELIVERY_COMPLETE":
            case "1-EXCHANGE_REQUEST":
            case "1-EXCHANGE_PROCESS":
            case "1-EXCHANGE":
            case "1-RETURN_REQUEST":
            case "1-RETURN":
            case "1-RETURN_PROCESS":
            case "1-CLAIM_CANCEL":
            case "1-OTHER":
                argoOrderTemplate = getPlayerArgoOrderTemplate();
                orderAddressTemplate = getPlayerOrderAddressTemplate();
                vendorItemLifecycleTemplate = getPlayerOrderVendorItemLifecycleTemplate();
                templateMap.put(vendorItemLifecycleTemplate.getTargetId(), vendorItemLifecycleTemplate);
                templateMap.put(orderAddressTemplate.getTargetId(), orderAddressTemplate);
                templateMap.put(argoOrderTemplate.getTargetId(), argoOrderTemplate);
                break;
        }
        return templateMap;

    }
}
