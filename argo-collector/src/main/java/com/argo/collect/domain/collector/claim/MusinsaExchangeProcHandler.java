package com.argo.collect.domain.collector.claim;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MusinsaExchangeProcHandler implements MusinsaClaimHandler {
    @Override
    public boolean isClaim(Map rowData) {
        String claimState = String.valueOf(rowData.get("CLM_STATE_NM"));
        return "교환처리".equals(claimState);
    }

    @Override
    public Map makeClaim(Map rowData) {
        Map exchange = Maps.newHashMap(rowData);
        this.modifyOriginalData(rowData);
        return exchange;
    }

    @Override
    public void modifyOriginalData(Map rowData) {
        rowData.put("clm_state","");
    }
}
