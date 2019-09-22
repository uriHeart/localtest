package com.argo.common.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMetadata {
    private Long totalQuantity;
    private Long totalPrice;
    private Long deliveryPrice;
    private Long cancelPrice;
    private Long cancelDeliveryPrice;
    private Date collectedAt;
    private Date orderedAt;
}
