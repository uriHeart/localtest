package com.argo.common.domain.vendor.item;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class VendorItemService {
    @Autowired
    private SourceItemMappingRepository sourceItemMappingRepository;

    private Map<String, SourceItemMapping> sourceItemMappingMap;

    private final static String DELIMITER = "_";

    @PostConstruct
    public void init() {
        sourceItemMappingMap = Maps.newHashMap();
        List<SourceItemMapping> mappings = sourceItemMappingRepository.findAllBy();
        mappings.forEach(
                m -> {
                    String keyPrefix = this.getKeyPrefix(m.getVendor().getVendorId(), m.getSalesChannel().getSalesChannelId());
                    sourceItemMappingMap.put(keyPrefix + m.getItemId(), m);
                    sourceItemMappingMap.put(keyPrefix + m.getSourceItemKey(), m);
                }
        );
    }

    private String getKeyPrefix(Long vendorId, Long channelId) {
        StringJoiner strJoiner = new StringJoiner(DELIMITER);
        strJoiner.add(String.valueOf(vendorId)).add(String.valueOf(channelId));
        return strJoiner.toString() + DELIMITER;
    }

    public VendorItem getVendorItem(Long vendorId, Long channelId, String sourceItem) {
        return sourceItemMappingMap.getOrDefault(this.getKeyPrefix(vendorId, channelId) + sourceItem,
                SourceItemMapping.builder().build()).getVendorItem();
    }
}
