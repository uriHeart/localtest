package com.argo.common.domain.order.doc;

import lombok.*;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDoc {
    private String id;
    private String orderId;
    private Long vendorId;
    private String salesChannelCode;
    private Date orderedAt;
}
