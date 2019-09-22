package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderAddressDto {
    private OriginalAddressDto originalAddress; //고객 원본 주소
    private RefinedAddressDto refinedAddress; //고객 정제 주소 (현재 없음)
    private RecipientDto recipient; //수취인 정보
    private OrdererDto orderer; //주문자 정보
    private DeliveryRequestDto deliveryRequest; //배송요청사항 - 문앞에 배송바랍니다..등
}
