package com.argo.common.domain.sku;

import lombok.*;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SkuData {
    private final static String ATTR_COLOR = "COLOR";
    private final static String ATTR_SIZE = "SIZE";
    private Long skuId;
    private Long vendorId;
    private String name;
    private String brand;
    private String barcode;
    private List<SkuAttributeData> attributes;

    public String getColor() {
        Optional<SkuAttributeData> data = attributes.stream().filter(a -> ATTR_COLOR.equals(a.getAttributeKey())).findFirst();
        if (data.isPresent()) {
            return data.get().getAttributeValue();
        }
        return null;
    }

    public String getSize() {
        Optional<SkuAttributeData> data = attributes.stream().filter(a -> ATTR_SIZE.equals(a.getAttributeKey())).findFirst();
        if (data.isPresent()) {
            return data.get().getAttributeValue();
        }
        return null;
    }
}
