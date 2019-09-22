package com.argo.common.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orderer {
    private String name; //주문자 이름
    private String phoneNumber1; //주문자 전화번호1
    private String phoneNumber2; //주문자 전화번호2
}
