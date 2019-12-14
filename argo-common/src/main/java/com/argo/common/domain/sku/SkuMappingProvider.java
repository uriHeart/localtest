package com.argo.common.domain.sku;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.vendor.item.VendorItem;
import com.argo.common.domain.vendor.item.VendorItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConversionOperationService
public class SkuMappingProvider {
    private final static Long EZ_ADMIN_CHANNEL_ID = 2L;
    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private SkuAttributeRepository skuAttributeRepository;

    @Autowired
    private SkuMappingRepository skuMappingRepository;

    @Autowired
    private VendorItemService vendorItemService;

    private Map<Long, List<SkuMapping>> skuMappings;
    private Map<Long, SkuData> skuData;

    @PostConstruct
    public void init() {
        skuMappings = Maps.newHashMap();
        skuData = Maps.newHashMap();

        List<SkuMapping> mappings = skuMappingRepository.findAllBy();
        mappings.forEach(
                m -> {
                    List<SkuMapping> targets = skuMappings.getOrDefault(m.getVendorItem().getVendorItemId(), Lists.newArrayList());
                    targets.add(m);
                    skuMappings.put(m.getVendorItem().getVendorItemId(), targets);
                }
        );

        skuRepository.findAll().forEach(s -> skuData.put(s.getSkuId(), SkuData.builder()
                .skuId(s.getSkuId())
                .vendorId(s.getVendorId())
                .name(s.getName())
                .barcode(s.getBarcode())
                .brand(s.getBrand())
                .attributes(Lists.newArrayList())
                .build()));

        for (SkuAttributeData sa : skuAttributeRepository.findAllSkuAttributeData()) {
            if (skuData.containsKey(sa.getSkuId())) {
                List<SkuAttributeData> data = skuData.get(sa.getSkuId()).getAttributes();
                data.add(sa);
            }
        }
    }

    @ConversionOperationMethod
    public List<Long> getSkuIds(Long channelId, Long vendorId, String sourceItemName, String sourceItemOption) {
        StringJoiner strJoiner = new StringJoiner("_");
        strJoiner.add(sourceItemName).add(sourceItemOption);
        String key = Hashing.sha256().hashString(strJoiner.toString(), StandardCharsets.UTF_8).toString();
        return this.getSkuIds(channelId, vendorId, key);
    }

    public List<Long> getSkuIds(Long channelId, Long vendorId, String sourceItemCode) {
        VendorItem vendorItem = vendorItemService.getVendorItem(vendorId, channelId, sourceItemCode);
        vendorItem = vendorItem == null
                ? vendorItemService.getVendorItem(vendorId, EZ_ADMIN_CHANNEL_ID, sourceItemCode)
                : vendorItem;
        if (vendorItem == null) {
            return Collections.emptyList();
        }

        return skuMappings.getOrDefault(vendorItem.getVendorItemId(), Lists.newArrayList()).stream()
                .map(m -> m.getSku().getSkuId()).collect(Collectors.toList());
    }

    public List<SkuData> getSkus(List<Long> skus) {
        return skus.stream().map(s -> skuData.getOrDefault(s, SkuData.builder().build())).collect(Collectors.toList());
    }
}
