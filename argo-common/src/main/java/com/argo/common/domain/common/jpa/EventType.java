package com.argo.common.domain.common.jpa;

import java.util.Arrays;

public enum EventType {
    ORDER("ORDER"), //주문완료
    CANCEL("CANCEL"), //주문취소
    PAYMENT_COMPLETE("PAYMENT_COMPLETE"),//결제완료
    RELEASE_REQUEST("RELEASE_REQUEST"),//출고요청
    RELEASE("RELEASE"),//출고완료
    DELIVERY("DELIVERY"),//배송
    DELIVERY_END("DELIVERY_END"),//배송완료
    EXCHANGE("EXCHANGE"), //교환
    RETURN("RETURN"), //환불
    OTHER("OTHER");


    private String value;

    EventType(String value) {
        this.value = value;
    }

    public static EventType of(String event){
        return Arrays.stream(values())
                .filter(eventType -> event.equals(eventType.value))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("no matched event type"));
    }}
