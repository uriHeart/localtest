package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSkuDto {
    private String barcode;
    private Long skuId;
    private String skuName;
    private String skuSize;
    private String skuColor;
}
