package com.argo.collect.domain.collector.claim;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  교환요청에 의한 출고
 */
@Component
public class MusinsaExChangeReleaseHandler implements MusinsaClaimHandler{
    @Override
    public boolean isClaim(Map rowData) {
        String ordStateNm = String.valueOf(rowData.get("ORD_STATE_NM"));
        String parentOrdNo = String.valueOf(rowData.get("P_ORD_NO"));

        return "출고요청".equals(ordStateNm) && !StringUtils.isEmpty(parentOrdNo);
    }


    @Override
    public Map makeClaim(Map rowData) {
        this.modifyOriginalData(rowData);
        return new HashMap();
    }

    @Override
    public void modifyOriginalData(Map rowData) {
        //P_ORD_NO 가 있으면 0원처리
        rowData.put("price","0");
        rowData.put("ord_state","교환요청");

    }
}
