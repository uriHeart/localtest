package com.argo.collect.domain.collector.claim;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 교환요청 자식주문에대한 클레임무효
 */
@Component
public class MusinsaExChangeParentClaimCancelHandler implements MusinsaClaimHandler {

    @Override
    public boolean isClaim(Map rowData) {
        String claimState = String.valueOf(rowData.get("CLM_STATE_NM"));
        return "클레임무효".equals(claimState) && "n".equals(rowData.get("REFUND_YN"));
    }

    @Override
    public Map makeClaim(Map rowData) {
        Map claimCancel = Maps.newHashMap(rowData);

        claimCancel.put("upd_date",claimCancel.get("LAST_UP_DATE"));
        claimCancel.put("price","0");
        claimCancel.put("qty", Integer.parseInt(String.valueOf(rowData.get("qty")))* -1);

        this.modifyOriginalData(rowData);
        return claimCancel;
    }

    @Override
    public void modifyOriginalData(Map rowData) {
        rowData.put("clm_state","");
    }
}
