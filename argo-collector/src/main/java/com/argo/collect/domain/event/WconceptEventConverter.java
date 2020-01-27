package com.argo.collect.domain.event;

import com.argo.common.domain.common.jpa.EventType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WconceptEventConverter implements EventConverter {
    @Override
    public boolean isSupport(String channel) {
        return "W_CONCEPT".equals(channel);
    }

    @Override
    public EventType getEventType(Map source) {
        String orderState = String.valueOf(source.get("주문상태"));
        if("결제완료".equals(orderState)) return EventType.PAYMENT_COMPLETE;
        if("상품준비중".equals(orderState)) return EventType.RELEASE;
        if("배송중".equals(orderState)) return EventType.DELIVERY;
        if("배송완료".equals(orderState)) return EventType.DELIVERY_COMPLETE;
        if("반품처리중".equals(orderState)) return EventType.RETURN_PROCESS;
        if("교환처리중".equals(orderState)) return EventType.EXCHANGE_PROCESS;
        if("교환배송완료".equals(orderState)) return EventType.EXCHANGE;
        if("반품완료".equals(orderState)) return EventType.RETURN;
        if("취소완료".equals(orderState)) return EventType.CANCEL;

        return EventType.ORDER;
    }
}
