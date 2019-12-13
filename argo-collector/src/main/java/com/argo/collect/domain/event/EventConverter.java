package com.argo.collect.domain.event;

import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.vendor.VendorChannel;

import java.util.Map;

public interface EventConverter {
    boolean isSupport(String channel);
    public EventType getEventType(Map source);

}
