package com.argo.common.domain.sku;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SkuMappingProvider {
    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private SkuAttributeRepository skuAttributeRepository;

    @Autowired
    private SourceItemRepository sourceItemRepository;

    private Map<String, SourceItem> items;
    private Map<String, SkuData> skus;

    @PostConstruct
    public void init() {
        items = Maps.newHashMap();
        skus = Maps.newHashMap();
        sourceItemRepository.findAllLazy().forEach(i -> items.put(
                this.getItemIdKey(i.getSalesChannel().getSalesChannelId(), i.getVendor().getVendorId(), i.getItemId()), i));

        skuRepository.findAll().forEach(s -> {
            skus.put(s.getBarcode(), SkuData.builder()
                    .skuId(s.getSkuId())
                    .name(s.getName())
                    .barcode(s.getBarcode())
                    .brand(s.getBrand())
                    .attributes(Lists.newArrayList())
                    .build());
        });

        for (SkuAttributeData sa : skuAttributeRepository.findAllSkuAttributeData()) {
            if (!skus.containsKey(sa.getBarcode())) {
                continue;
            }
            List<SkuAttributeData> data = skus.get(sa.getBarcode()).getAttributes();
            data.add(sa);
        }
    }

    private String getItemIdKey(Long channelId, Long vendorId, String sourceItemCode) {
        return channelId + "-" + vendorId + "-" + sourceItemCode;
    }

    public String getSkuNameByItemCode(Long channelId, Long vendorId, String sourceItemCode) {
        if (!items.containsKey(this.getItemIdKey(channelId, vendorId, sourceItemCode))) {
            return null;
        }
        String barcode = items.get(this.getItemIdKey(channelId, vendorId, sourceItemCode)).getBarcode();
        return skus.get(barcode).getName();
    }

    public String getSkuNameByBarcode(String barcode) {
        if (!items.containsKey(barcode)) {
            return null;
        }
        return skus.get(barcode).getName();
    }

    public String getSkuColorByItemCode(Long channelId, Long vendorId, String sourceItemCode) {
        if (!items.containsKey(this.getItemIdKey(channelId, vendorId, sourceItemCode))) {
            return null;
        }
        String barcode = items.get(this.getItemIdKey(channelId, vendorId, sourceItemCode)).getBarcode();
        return skus.get(barcode).getColor();
    }

    public String getSkuColorByBarcode(String barcode) {
        if (!items.containsKey(barcode)) {
            return null;
        }
        return skus.get(barcode).getColor();
    }

    public String getSkuSizeByItemCode(Long channelId, Long vendorId, String sourceItemCode) {
        if (!items.containsKey(this.getItemIdKey(channelId, vendorId, sourceItemCode))) {
            return null;
        }
        String barcode = items.get(this.getItemIdKey(channelId, vendorId, sourceItemCode)).getBarcode();
        return skus.get(barcode).getSize();
    }

    public String getSkuSizeByBarcode(String barcode) {
        if (!items.containsKey(barcode)) {
            return null;
        }
        return skus.get(barcode).getSize();
    }
}
