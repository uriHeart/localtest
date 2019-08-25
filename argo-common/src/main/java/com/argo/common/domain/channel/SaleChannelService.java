package com.argo.common.domain.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleChannelService {
    @Autowired
    private SalesChannelRepository salesChannelRepository;

    @Autowired
    private ChannelCollectInfoRepository channelCollectInfoRepository;

    public List<SalesChannel> listAll() {
        return salesChannelRepository.findAll();
    }
}
