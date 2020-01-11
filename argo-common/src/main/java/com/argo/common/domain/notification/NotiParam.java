package com.argo.common.domain.notification;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotiParam {
    private String text;
}
