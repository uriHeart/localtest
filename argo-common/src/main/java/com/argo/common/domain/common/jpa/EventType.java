package com.argo.common.domain.common.jpa;

import java.util.Arrays;

public enum EventType {
    RELEASE("RELEASE"),ORDER("ORDER"),EXCHANGE("EXCHANGE"),RETURN("RETURN")
    ,OTHER("OTHER"),CANCEL("CANCEL");


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
