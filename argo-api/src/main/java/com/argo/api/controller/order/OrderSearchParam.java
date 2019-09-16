package com.argo.api.controller.order;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSearchParam {
    private String salesChannelCode;
    private Date from;
    private Date to;
    private String orderId;
}
