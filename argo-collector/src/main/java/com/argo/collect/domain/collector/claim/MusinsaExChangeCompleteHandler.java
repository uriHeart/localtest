package com.argo.collect.domain.collector.claim;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 교환 요청상태로 종결되는 데이터가 있음
 * 교환요청 갯수 -1
 * 교환처리 갯수 +1
 * 교환완료 갯수 -1
 */
@Component
public class MusinsaExChangeCompleteHandler implements MusinsaClaimHandler{
    @Override
    public boolean isClaim(Map rowData) {
        String claimState = String.valueOf(rowData.get("CLM_STATE_NM"));
        return "교환완료".equals(claimState);
    }


    /**
     * 자식주문을 만들어야 하지만 힘들다!
     * 어차피 부모 자식관계로 안갈꺼면 임시데이터는 나중에 만들자
     * @param rowData
     * @return
     */
    @Override
    public Map makeClaim(Map rowData) {
        Map exchange = Maps.newHashMap(rowData);
        exchange.put("upd_date",rowData.get("LAST_UP_DATE"));
        exchange.put("qty", Integer.parseInt(String.valueOf(rowData.get("qty")))* -1);

        this.modifyOriginalData(rowData);
        return exchange;
    }

    @Override
    public void modifyOriginalData(Map rowData) {
        rowData.put("clm_state","");
    }
}
