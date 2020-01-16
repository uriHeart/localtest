package com.argo.collect.domain.event;

import com.argo.common.domain.common.jpa.EventType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MusinsaEventConverter implements EventConverter {

    @Override
    public boolean isSupport(String channel) {
        return "MUSINSA".equals(channel);
    }

    @Override
    public EventType getEventType(Map source) {

        String clmState = String.valueOf(source.get("clm_state"));
        String ordState = String.valueOf(source.get("ord_state"));
        if("클레임무효".equals(clmState)) return EventType.CLAIM_CANCEL;
        if("교환요청".equals(clmState)) return EventType.EXCHANGE_REQUEST;
        if("환불요청".equals(clmState)) return EventType.RETURN_REQUEST;
        if("교환처리".equals(clmState)) return EventType.EXCHANGE_PROCESS;
        if("환불처리".equals(clmState)) return EventType.RETURN_PROCESS;
        if("교환완료".equals(clmState)) return EventType.EXCHANGE;
        if("환불완료".equals(clmState)) return EventType.RETURN;
        if("입금예정".equals(ordState)) return EventType.ORDER;
        if("주문취소".equals(ordState) || "결제오류".equals(ordState)) return EventType.CANCEL;
        if("출고요청".equals(ordState) || "입금확인".equals(ordState)) return EventType.PAYMENT_COMPLETE;
        if("출고처리중".equals(ordState)) return EventType.RELEASE_REQUEST;
        if("출고완료".equals(ordState)) return EventType.RELEASE;
        if("배송시작".equals(ordState)) return EventType.DELIVERY;
        if("배송완료".equals(ordState) || "구매확정".equals(ordState)) return EventType.DELIVERY_COMPLETE;

        return EventType.OTHER;
    }
}
