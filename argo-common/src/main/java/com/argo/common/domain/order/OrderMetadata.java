package com.argo.common.domain.order;

import com.argo.common.domain.common.data.TargetData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMetadata extends TargetData {
    private Long totalQuantity;
    private Long totalPrice;
    private Long deliveryPrice;
    private Long cancelPrice;
    private Long cancelDeliveryPrice;
    // private Date collectedAt; // dataRows.collect_date + dataRows.collect_time
    private Date orderedAt; // dataRows.order_date + dataRows.order_time
}
