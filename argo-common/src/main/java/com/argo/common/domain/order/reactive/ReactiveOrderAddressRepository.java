package com.argo.common.domain.order.reactive;

import com.argo.common.domain.order.OrderAddress;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveOrderAddressRepository extends ReactiveCassandraRepository<OrderAddress, Long> {
    Mono<OrderAddress> findFirstByVendorIdAndChannelIdAndOrderIdOrderByPublishedAtDesc(Long vendorId, Long channelId, String orderId);
}
