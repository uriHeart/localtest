package com.argo.api.controller.order;

import com.argo.common.domain.order.doc.OrderDoc;
import com.argo.common.domain.order.*;
import com.argo.common.domain.order.dto.OrderResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/order")
    public Mono<Void> addOrder(@RequestParam Long vendorId, @RequestParam Long channelId,
                               @RequestParam String orderId, @RequestParam String vendorItemId) {

        return orderService.addOrder(vendorId, channelId, orderId, vendorItemId)
                .subscribeOn(Schedulers.elastic())
                .timeout(Duration.ofMillis(1000)).retry(3)
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
