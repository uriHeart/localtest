package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderProductDto {
    private String vendorItemId; //벤더아이템 아이디
    private String productId; // 상품아이디
    private String productName; // 상품명
    private String productDesc; // 상풍 설명
    private Long quantity; // 상품 수량
    private ProductPriceDto productPrice; // 상품 가격
    private String applyType; // 주문, 교환, 취소(배송전), 반품(배송후)
}

