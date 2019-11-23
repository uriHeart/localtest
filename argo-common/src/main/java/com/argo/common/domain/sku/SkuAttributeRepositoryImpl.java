package com.argo.common.domain.sku;

import com.argo.common.configuration.ArgoQueryDslRepositorySupport;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;

import java.util.List;

public class SkuAttributeRepositoryImpl extends ArgoQueryDslRepositorySupport implements SkuAttributeRepositoryCustom {
    private QSku qSku = QSku.sku;
    private QSkuAttribute qSkuAttribute = QSkuAttribute.skuAttribute;
    public SkuAttributeRepositoryImpl() {
        super(SkuAttribute.class);
    }

    @Override
    public List<SkuAttributeData> findAllSkuAttributeData() {
        ConstructorExpression<SkuAttributeData> constructor = Projections
                .constructor(SkuAttributeData.class
                        ,qSku.skuId
                        ,qSku.barcode
                        ,qSkuAttribute.attributeKey
                        ,qSkuAttribute.attributeValue
                );
        return from(qSkuAttribute)
                .innerJoin(qSkuAttribute.sku, qSku)
                .select(constructor).fetch();
    }
}
