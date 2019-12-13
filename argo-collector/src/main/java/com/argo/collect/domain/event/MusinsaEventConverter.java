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

        if(source.get("clm_state")!=null && source.get("clm_state").equals("교환완료")) return EventType.EXCHANGE;
        if(source.get("clm_state")!=null && source.get("clm_state").equals("환불완료")) return EventType.RETURN;
        if(source.get("ord_state").equals("입금예정")) return EventType.ORDER;
        if(source.get("ord_state").equals("주문취소")||source.get("ord_state").equals("결제오류")) return EventType.CANCEL;
        if(source.get("ord_state").equals("입금확인")) return EventType.PAYMENT_COMPLETE;
        if(source.get("ord_state").equals("출고요청")||source.get("ord_state").equals("출고처리중")) return EventType.RELEASE_REQUEST;
        if(source.get("ord_state").equals("출고완료")) return EventType.RELEASE;
        if(source.get("ord_state").equals("배송시작")) return EventType.DELIVERY;
        if(source.get("ord_state").equals("배송완료")||source.get("ord_state").equals("구매확정")) return EventType.DELIVERY_END;

        return EventType.OTHER;
    }
}
