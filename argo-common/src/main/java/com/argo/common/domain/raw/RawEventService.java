package com.argo.common.domain.raw;

import com.argo.common.domain.common.jpa.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class RawEventService {
    @Autowired
    private RawEventRepository rawEventRepository;

    public void save(RawEvent rawEvent) {
        if (rawEvent.getEvent() == null) {
            List<RawEvent> rawEvents = rawEventRepository.findByVendorIdAndChannelIdAndOrderId(rawEvent.getVendorId(), rawEvent.getChannelId(), rawEvent.getOrderId());
            if (rawEvents.isEmpty()) {
                rawEvent.setEvent(EventType.ORDER.toString());
            } else {
                rawEvent.setEvent(EventType.OTHER.toString());
            }
        }
        rawEventRepository.save(rawEvent);
    }
}
