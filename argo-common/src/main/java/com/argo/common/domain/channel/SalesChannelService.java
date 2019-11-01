package com.argo.common.domain.channel;

import com.argo.common.domain.vendor.VendorService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class SalesChannelService {
    @Autowired
    private SalesChannelRepository salesChannelRepository;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ChannelCollectInfoRepository channelCollectInfoRepository;

    private Map<Long, SalesChannel> salesChannelMap;

    @PostConstruct
    public void init() {
        salesChannelMap = Maps.newHashMap();
        listAll().forEach(
            channel -> {
                salesChannelMap.put(channel.getSalesChannelId(), channel);
            }
        );
    }

    public List<SalesChannel> listAll() {
        return salesChannelRepository.findAll();
    }

    public ChannelCollectInfo getChannelCollectInfo(SalesChannel salesChannel) {
        return channelCollectInfoRepository.findBySalesChannel(salesChannel);
    }

    public SalesChannel getSalesChannel(Long salesChannelId) {
        return salesChannelMap.get(salesChannelId);
    }
}
