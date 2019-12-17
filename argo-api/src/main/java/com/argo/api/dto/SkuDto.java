package com.argo.api.dto;

import com.argo.common.domain.sku.Sku;
import com.argo.common.domain.sku.SkuAttribute;
import com.argo.common.domain.sku.SkuVo;
import lombok.ToString;
import org.apache.commons.compress.utils.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<SkuAttributeDto> getSkuAttributes() {
        return skuVo.getSkuAttributes().stream()
                .map(SkuAttributeDto::new)
                .collect(Collectors.toList());
//        return Lists.newArrayList();
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
