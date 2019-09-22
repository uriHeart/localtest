package com.argo.common.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackedReason {
    private String reasonCode;
    private String reasonName;
    private String reasonDetail;
}
