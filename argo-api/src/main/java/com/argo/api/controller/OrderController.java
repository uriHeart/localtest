package com.argo.api.controller;

import com.argo.common.domain.order.OrderDoc;
import com.argo.common.domain.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@RestController
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/{orderId}")
    public Mono<Void> addOrder(@PathVariable String orderId) {

        return orderService.addOrder(OrderDoc.builder()
                    .id(orderId)
                    .orderId(orderId)
                    .orderedAt(new Date()).build())
                .onErrorResume(error -> {
                    log.error("index error ", error);
                    return Mono.empty();
                });
    }
}
