package com.argo.api.controller.channel;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SalesChannelDto {
    private Long salesChannelId;
    private String salesChannelCode;
    private String salesChannelName;
    private String baseUrl;
}
