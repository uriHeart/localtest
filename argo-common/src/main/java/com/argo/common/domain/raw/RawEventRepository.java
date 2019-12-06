package com.argo.common.domain.raw;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RawEventRepository extends CassandraRepository<RawEvent, Long> {
    List<RawEvent> findByVendorIdAndChannelIdAndOrderId(Long vendorId, Long channelId, String orderId);
    RawEvent findFirstByVendorIdAndChannelIdAndOrderId(Long vendorId, Long channelId, String orderId);

    RawEvent findByVendorIdAndChannelIdAndOrderIdAndPublishedAt(Long vendorId, Long channelId, String orderId, Date publishedAt);

}
