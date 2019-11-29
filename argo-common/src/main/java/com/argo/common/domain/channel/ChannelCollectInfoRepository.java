package com.argo.common.domain.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelCollectInfoRepository extends JpaRepository<ChannelCollectInfo, Long> {
    ChannelCollectInfo findBySalesChannel(SalesChannel salesChannel);
    List<ChannelCollectInfo> findAllBySalesChannel(SalesChannel salesChannel);

}
