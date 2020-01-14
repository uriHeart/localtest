package com.argo.collect.domain.collector.claim;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 201910152247506750 : 실제환불무효   (처리하는 케이스)
 * 201910011059165294 : 환불무효 이지만 환불해줌
 */
@Component
public class MusinsaRefundClaimCancelHandler implements MusinsaClaimHandler{
    @Override
    public boolean isClaim(Map rowData) {
        String claimState = String.valueOf(rowData.get("CLM_STATE_NM"));
        return "클레임무효".equals(claimState) && "y".equals(rowData.get("REFUND_YN"));
    }

    @Override
    public Map makeClaim(Map rowData) {
        Map claimCancel = Maps.newHashMap(rowData);

        this.modifyOriginalData(rowData);
        return claimCancel;
    }

    @Override
    public void modifyOriginalData(Map rowData) {
        rowData.put("clm_state","");
    }
}
