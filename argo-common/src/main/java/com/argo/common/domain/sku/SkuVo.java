package com.argo.common.domain.sku;

import lombok.ToString;
import org.apache.commons.compress.utils.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class SkuVo {
    private Sku sku;

    public SkuVo(Sku sku) {
        this.sku = sku;
    }

    public Long getSkuId() {
        return sku.getSkuId();
    }

    public String getBarcode() {
        return sku.getBarcode();
    }

    public Double getLength() {
        return sku.getLength();
    }

    public Double getWidth() {
        return sku.getWidth();
    }

    public Double getHeight() {
        return sku.getHeight();
    }

    public Double getWeight() {
        return sku.getWeight();
    }

    public String getName() {
        return sku.getName();
    }

    public Long getVendorId() {
        return sku.getVendorId();
    }

    public String getDescription() {
        return sku.getDescription();
    }

    public Sku getParentSku() {
        return sku.getParentSku();
    }

    public String getImageUrl() {
        return sku.getImageUrl();
    }

    public String getBrand() {
        return sku.getBrand();
    }

    public List<SkuAttributeVo> getSkuAttributes() {
        return sku.getSkuAttributes().stream()
                .map(SkuAttributeVo::new)
                .collect(Collectors.toList());
    }

    public Date getCreatedAt() {
        return sku.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return sku.getUpdatedAt();
    }

    public Date getCreatedBy() {
        return sku.getCreatedBy();
    }

    public Date getUpdatedBy() {
        return sku.getUpdatedBy();
    }
}
