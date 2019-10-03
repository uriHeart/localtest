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
    private Long channelId;
    private String salesChannelCode;
    private Date orderedAt;
    private Date collectedAt;

    private String orderStatus;

    private String vendorItemId;
    private String vendorItemLifeCycleStatus;
    private Long quantity;

    private String paymentMethod;
    private Long originalPrice;
    private Long salesPrice;
    private Long paymentAmount;

    private String sourceItemId;
    private String sourceItemName;
    private String sourceItemOption;

    public Long getVendorId() {
        return vendorId == null ? 1L : vendorId;
    }

    public Long getChannelId() {
        return channelId == null ? 1L : channelId;
    }
}