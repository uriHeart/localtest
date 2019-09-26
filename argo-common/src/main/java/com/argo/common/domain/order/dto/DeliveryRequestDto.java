<<<<<<< Updated upstream:argo-common/src/main/java/com/argo/common/domain/order/dto/DeliveryRequestDto.java
package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryRequestDto {
    private String deliveryRequest; //배송요청사항 - 문앞, 경비실 ...
}
=======
package com.argo.common.domain.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryRequestDto {
    private String deliveryRequest; //배송요청사항 - 문앞, 경비실 ...
}
>>>>>>> Stashed changes:argo-common/src/main/java/com/argo/common/domain/order/DeliveryRequestDto.java
