package com.argo.common.domain.order.vendoritem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuMappingData {
    private List<SkuMapping> skuMappingList;
}
