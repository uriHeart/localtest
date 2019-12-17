package com.argo.common.domain.sku;

import com.argo.common.configuration.ArgoQueryDslRepositorySupport;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;

import java.util.List;
import java.util.stream.Collectors;

public class SkuRepositoryImpl extends ArgoQueryDslRepositorySupport implements SkuRepositoryCustom {
    private QSku qSku = QSku.sku;
    private QSkuAttribute qSkuAttribute = QSkuAttribute.skuAttribute;

    public SkuRepositoryImpl() {
        super(Sku.class);
    }

    @Override
    public List<Sku> findByVendorId(Long vendorId, Long fromSkuId, Long limit) {
        return from(qSku)
                .innerJoin(qSku.skuAttributes, qSkuAttribute).fetchJoin()
                .where(qSku.skuId.goe(fromSkuId))
                .limit(limit)
                .select(qSku).fetch().stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
