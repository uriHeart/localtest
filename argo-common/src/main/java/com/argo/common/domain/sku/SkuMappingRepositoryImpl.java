package com.argo.common.domain.sku;

import com.argo.common.configuration.ArgoQueryDslRepositorySupport;
import com.argo.common.domain.vendor.item.QVendorItem;

import java.util.List;

public class SkuMappingRepositoryImpl extends ArgoQueryDslRepositorySupport implements SkuMappingRepositoryCustom {
    private QSkuMapping qSkuMapping = QSkuMapping.skuMapping;
    private QSku qSku = QSku.sku;
    private QVendorItem qVendorItem = QVendorItem.vendorItem;
    public SkuMappingRepositoryImpl() {
        super(SkuMapping.class);
    }

    @Override
    public List<SkuMapping> findAllBy() {
        return from(qSkuMapping)
                .innerJoin(qSkuMapping.vendorItem, qVendorItem).fetchJoin()
                .innerJoin(qSkuMapping.sku, qSku).fetchJoin()
                .select(qSkuMapping).fetch();
    }
}
