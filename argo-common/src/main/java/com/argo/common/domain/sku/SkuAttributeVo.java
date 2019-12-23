package com.argo.common.domain.sku;

import lombok.ToString;

import java.util.Date;

@ToString
public class SkuAttributeVo {
    private SkuAttribute skuAttribute;

    public SkuAttributeVo(SkuAttribute skuAttribute) {
        this.skuAttribute = skuAttribute;
    }

    public Long getSkuAttributeId() {
        return skuAttribute.getSkuAttributeId();
    }

    public String getAttributeKey() {
        return skuAttribute.getAttributeKey();
    }

    public String getAttributeValue() {
        return skuAttribute.getAttributeValue();
    }

    public Date getCreatedAt() {
        return skuAttribute.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return skuAttribute.getUpdatedAt();
    }

    public Date getCreatedBy() {
        return skuAttribute.getCreatedBy();
    }

    public Date getUpdatedBy() {
        return skuAttribute.getUpdatedBy();
    }
}
