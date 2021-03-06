package com.argo.common.domain.order.vendoritem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderVendorItemMetadata {
    private String applyType;
    private String paymentMethod; // 결제방법 - 카드, 현금, point, 가상계좌, 핸드폰 (dataRows.pay_type)
    private Long originalPrice; // 판매원가 (dataRows.org_price)
    private Long salesPrice; // 실판매가 (dataRows.shop_price)
    private Long paymentAmount; // 결제금액 (dataRows.amount)
}
