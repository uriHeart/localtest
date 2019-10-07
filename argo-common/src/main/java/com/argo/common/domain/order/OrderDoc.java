package com.argo.common.domain.order;

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
    private Date orderedAt;
}
