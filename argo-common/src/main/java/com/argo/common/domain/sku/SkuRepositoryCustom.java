package com.argo.common.domain.sku;

import java.util.List;

public interface SkuRepositoryCustom {
    public List<Sku> findByVendorId(Long vendorId);
}
