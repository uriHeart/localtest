<<<<<<< Updated upstream:argo-common/src/main/java/com/argo/common/domain/order/dto/OrdererDto.java
package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrdererDto {
    private String name; //주문자 이름
    private String phoneNumber1; //주문자 전화번호1
    private String phoneNumber2; //주문자 전화번호2
}
=======
package com.argo.common.domain.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrdererDto {
    private String name; //주문자 이름
    private String phoneNumber1; //주문자 전화번호1
    private String phoneNumber2; //주문자 전화번호2
}
>>>>>>> Stashed changes:argo-common/src/main/java/com/argo/common/domain/order/OrdererDto.java
