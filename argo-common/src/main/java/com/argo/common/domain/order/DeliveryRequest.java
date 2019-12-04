package com.argo.common.domain.order;

import com.argo.common.domain.common.data.TargetData;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest extends TargetData {
    private String deliveryRequest; //배송요청사항 - 문앞, 경비실 ... (dataRows.memo)
}
