package com.argo.common.domain.order.reactive;

import com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ReactiveOrderVendorItemLifecycleRepository extends ReactiveCassandraRepository<OrderVendorItemLifecycle, Long> {
    Mono<OrderVendorItemLifecycle> findFirstByVendorIdAndChannelIdAndOrderIdAndVendorItemIdOrderByPublishedAtDesc(Long vendorId, Long channelId, String orderId, UUID vendorItemId);
}
