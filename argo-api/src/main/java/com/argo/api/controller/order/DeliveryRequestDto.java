package com.argo.api.controller.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryRequestDto {
    private String deliveryRequest; //배송요청사항 - 문앞, 경비실 ...
}
