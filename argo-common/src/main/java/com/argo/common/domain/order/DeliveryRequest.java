package com.argo.common.domain.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {
    private String deliveryRequest; //배송요청사항 - 문앞, 경비실 ...
}
