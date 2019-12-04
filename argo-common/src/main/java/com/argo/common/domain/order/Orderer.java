package com.argo.common.domain.order;

import com.argo.common.domain.common.data.TargetData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orderer extends TargetData {
    private String name; //주문자 이름 (dataRows.order_name)
    private String phoneNumber1; //주문자 전화번호1 (dataRows.order_tel1)
    private String phoneNumber2; //주문자 전화번호2 (dataRows.order_tel1)
}
