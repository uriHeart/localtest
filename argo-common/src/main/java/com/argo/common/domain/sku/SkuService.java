package com.argo.common.domain.sku;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    public List<SkuVo> findByVendorId(Long vendorId) {
        List<Sku> skuList = skuRepository.findByVendorId(vendorId);

        return skuList.stream()
                .map(SkuVo::new)
                .collect(Collectors.toList());
    }
}
