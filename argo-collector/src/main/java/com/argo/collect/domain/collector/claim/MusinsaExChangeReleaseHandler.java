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
        return !StringUtils.isEmpty(rowData.get("P_ORD_NO"));
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
    }
}
