package com.argo.api.dto;

import com.argo.common.domain.sku.Sku;
import com.argo.common.domain.sku.SkuVo;
import lombok.ToString;

import java.util.Date;

@ToString
public class SkuDto {
    private SkuVo skuVo;

    public SkuDto(SkuVo skuVo) {
        this.skuVo = skuVo;
    }

    public Long getSkuId() {
        return skuVo.getSkuId();
    }

    public String getBarcode() {
        return skuVo.getBarcode();
    }

    public Double getLength() {
        return skuVo.getLength();
    }

    public Double getWidth() {
        return skuVo.getWidth();
    }

    public Double getHeight() {
        return skuVo.getHeight();
    }

    public Double getWeight() {
        return skuVo.getWeight();
    }

    public String getName() {
        return skuVo.getName();
    }

    public Long getVendorId() {
        return skuVo.getVendorId();
    }

    public String getDescription() {
        return skuVo.getDescription();
    }

    public Sku getParentSku() {
        return skuVo.getParentSku();
    }

    public String getImageUrl() {
        return skuVo.getImageUrl();
    }

    public String getBrand() {
        return skuVo.getBrand();
    }

    public Date getCreatedAt() {
        return skuVo.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return skuVo.getUpdatedAt();
    }

    public Date getCreatedBy() {
        return skuVo.getCreatedBy();
    }

    public Date getUpdatedBy() {
        return skuVo.getUpdatedBy();
    }
}
