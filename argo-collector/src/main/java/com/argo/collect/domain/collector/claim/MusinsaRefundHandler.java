package com.argo.collect.domain.collector.claim;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MusinsaRefundHandler implements MusinsaClaimHandler{
    @Override
    public boolean isClaim(Map rowData) {
        return "y".equals(rowData.get("REFUND_YN"));
    }

    @Override
    public Map makeClaim(Map rowData) {

        Map refund = Maps.newHashMap(rowData);
            //refund data custom
            refund.put("upd_date",refund.get("LAST_UP_DATE"));
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
