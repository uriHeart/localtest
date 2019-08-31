package com.argo.common.domain.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesChannelService {
    @Autowired
    private SalesChannelRepository salesChannelRepository;

    @Autowired
    private ChannelCollectInfoRepository channelCollectInfoRepository;

    public List<SalesChannel> listAll() {
        return salesChannelRepository.findAll();
    }

    public ChannelCollectInfo getChannelCollectInfo(SalesChannel salesChannel) {
        return channelCollectInfoRepository.findBySalesChannel(salesChannel);
    }
}
