package com.argo.common.domain.order;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSearchParam {
    private String salesChannelCode;
    private Long salesChannelId;
    private Long vendorId;
    private Date from;
    private Date to;
    private String orderId;

    public Long getSalesChannelId() {
        return salesChannelId == null || salesChannelId == 0L ? null : salesChannelId;
    }
}
