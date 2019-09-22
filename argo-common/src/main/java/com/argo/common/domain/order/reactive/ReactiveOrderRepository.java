package com.argo.common.domain.order.reactive;

import com.argo.common.domain.order.ArgoOrder;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveOrderRepository extends ReactiveCassandraRepository<ArgoOrder, Long> {
    Mono<ArgoOrder> findFirstByVendorIdAndChannelIdAndOrderIdOrderByPublishedAtDesc(Long vendorId, Long channelId, String orderId);
}
