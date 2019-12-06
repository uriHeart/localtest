package com.argo.common.domain.common.jpa;

import java.util.Arrays;

public enum EventType {
    ORDER("ORDER"), //주문
    CANCEL("CANCEL"), //주문취소
    RELEASE("RELEASE"),//출고
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
