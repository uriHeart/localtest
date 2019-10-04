package com.argo.common.domain.raw;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawEventRepository extends CassandraRepository<RawEvent, Long> {
    RawEvent findByVendorIdAndChannelIdAndOrderId(Long vendorId, Long channelId, String orderId);
}
