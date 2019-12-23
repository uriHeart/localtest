package com.argo.api.dto;

import com.argo.common.domain.sku.SkuAttributeVo;

import java.util.Date;

public class SkuAttributeDto {
    private SkuAttributeVo skuAttributeVo;

    public SkuAttributeDto(SkuAttributeVo skuAttributeVo) {
        this.skuAttributeVo = skuAttributeVo;
    }

    public Long getSkuAttributeId() {
        return skuAttributeVo.getSkuAttributeId();
    }

    public String getAttributeKey() {
        return skuAttributeVo.getAttributeKey();
    }

    public String getAttributeValue() {
        return skuAttributeVo.getAttributeValue();
    }

    public Date getCreatedAt() {
        return skuAttributeVo.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return skuAttributeVo.getUpdatedAt();
    }

    public Date getCreatedBy() {
        return skuAttributeVo.getCreatedBy();
    }

    public Date getUpdatedBy() {
        return skuAttributeVo.getUpdatedBy();
    }
}
