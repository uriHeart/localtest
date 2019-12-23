package com.argo.common.domain.sku;

import com.google.common.collect.Lists;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    @Transactional
    public List<SkuVo> findByVendorId(Long vendorId, Long fromSkuId, Long limit) {
        List<Sku> skuList = skuRepository.findByVendorId(vendorId, fromSkuId, limit);

        return skuList.stream()
                .map(SkuVo::new)
                .collect(Collectors.toList());
    }
}
