package com.argo.common.domain.sku;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuAttributeData {
    private Long skuId;
    private String barcode;
    private String attributeKey;
    private String attributeValue;
}
