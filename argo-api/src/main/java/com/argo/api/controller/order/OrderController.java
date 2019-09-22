package com.argo.api.controller.order;

import com.argo.common.domain.order.doc.OrderDoc;
import com.argo.common.domain.order.*;
import com.argo.common.domain.order.dto.OrderResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Date;
import java.util.List;

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
                .subscribeOn(Schedulers.elastic())
                .timeout(Duration.ofMillis(500)).retry(3)
                .onErrorResume(error -> {
                    log.error("index error ", error);
                    return Mono.empty();
                });
    }

    @PostMapping("/orders")
    public Mono<List<OrderResultDto>> getOrders(@RequestBody OrderSearchParam param) {
        return orderService.getOrderData(param)
                .subscribeOn(Schedulers.elastic())
                .timeout(Duration.ofMillis(10000))
                .retry(3)
                .onErrorResume(error -> {
                    log.error("search error ", error);
                    return Mono.empty();
                });
    }
}
