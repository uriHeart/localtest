package com.argo.common.domain.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelCollectInfoRepository extends JpaRepository<ChannelCollectInfo, Long> {
    ChannelCollectInfo findBySalesChannel(SalesChannel salesChannel);
}
