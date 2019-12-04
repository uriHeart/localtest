package com.argo.common.domain.order;

import com.argo.common.domain.common.data.TargetData;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OriginalAddress extends TargetData {
    private String address1; //고객 원본 주소1
    private String address2; //고객 원본 주소2
    private String fullAddress; //고객 원본 전체주소 (dataRows.recv_add)
}
