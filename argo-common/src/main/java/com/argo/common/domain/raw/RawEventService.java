package com.argo.common.domain.raw;

import com.argo.common.domain.common.data.DataConversionService;
import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.order.ArgoOrder;
import com.argo.common.domain.order.OrderAddress;
import com.argo.common.domain.order.OrderService;
import com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Service
public class RawEventService {
    @Autowired
    private RawEventRepository rawEventRepository;

    @Autowired
    private DataConversionService dataConversionService;

    @Autowired
    private OrderService orderService;

    public void save(RawEvent rawEvent) {
        if (rawEvent.getEvent() == null) {
            List<RawEvent> rawEvents = rawEventRepository.findByVendorIdAndChannelIdAndOrderId(rawEvent.getVendorId(), rawEvent.getChannelId(), rawEvent.getOrderId());
            if (rawEvents.isEmpty()) {
                rawEvent.setEvent(EventType.ORDER.toString());
            } else {
                rawEvent.setEvent(EventType.OTHER.toString());
            }
        }

        Map<String, Object> map = dataConversionService.convert(rawEventRepository.save(rawEvent));
        orderService.saveOrder((ArgoOrder) map.get("com.argo.common.domain.order.ArgoOrder"), (OrderAddress) map.get("com.argo.common.domain.order.OrderAddress"), (List<OrderVendorItemLifecycle>) map.get("com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle"));
    }
}
