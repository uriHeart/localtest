package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductPriceDto {
    private String paymentMethod; // 결제방법 - 카드, 현금, point, 가상계좌, 핸드폰
    private Long originalPrice; // 판매원가
    private Long salesPrice; // 실판매가
    private Long paymentAmount; // 결제금액
}
