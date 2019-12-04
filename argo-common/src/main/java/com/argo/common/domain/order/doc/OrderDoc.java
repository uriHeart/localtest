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
    private String channelType;
    private String channelTypeDescription;
    private String salesChannelCode;
    private String salesChannelName;
    private Date publishedAt;
    private Date orderedAt;
    private Date collectedAt;
    private Date publishedDate;

    private String orderStatus;

    private String recipientName;
    private String originalPostalCode;

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

    private Long skuId;
    private String skuName;
    private String barcode;
    private String skuSize;
    private String skuColor;

    private Location location;

    public Long getVendorId() {
        return vendorId == null ? 1L : vendorId;
    }

    public Long getChannelId() {
        return channelId == null ? 1L : channelId;
    }
}
