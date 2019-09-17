package com.argo.common.domain.order;


import com.argo.common.domain.channel.SalesChannelDto;
import com.argo.common.domain.vendor.VendorDto;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResultDto {
    private String orderId; //주문아이디
    private Date orderedAt; //주문일시
    private Date collectedAt; //발주일시
    private Date paidAt; //결제일시

    private Long totalQuantity; //총 수량
    private String orderState; //오더 상태
    private String orderStateDescription; //오더 상태 설명

    private VendorDto vendor; //벤더 정보
    private SalesChannelDto salesChannel; //채널 정보
    private OrderProductDto orderProduct; //주문 상품정보

    private OrderAddressDto orderAddress; //주문 주소
}
