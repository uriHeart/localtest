package com.argo.collect.domain.collector.claim;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PlayerRefundHandler implements PlayerClaimHandler{
    @Override
    public boolean isClaim(Map rowData) {
        String clmState = String.valueOf(rowData.get("클레임상태"));
        return "환불요청".equals(clmState) || "환불완료".equals(clmState);
    }

    @Override
    public Map makeClaim(Map rowData) {

        Map refund = Maps.newHashMap(rowData);
            //refund data custom
            refund.put("upd_date",refund.get("최종처리일"));
            refund.put("price", Integer.parseInt(String.valueOf(refund.get("price")))* -1);
            refund.put("qty", Integer.parseInt(String.valueOf(refund.get("qty")))* -1);
        this.modifyOriginalData(rowData);
        return refund;
    }

    @Override
    public void modifyOriginalData(Map rowData) {
        rowData.put("clm_state","");
    }
}
