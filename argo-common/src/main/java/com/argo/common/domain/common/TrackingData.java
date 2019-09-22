package com.argo.common.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingData {
    private TrackedWorker trackedWorker;
    private Date trackedAt;
    private TrackedWorkplace trackedWorkplace;
    private TrackedStatus trackedStatus;
    private TrackedReason trackedReason;
}
