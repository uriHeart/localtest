package com.argo.common.domain.channel;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SalesChannelService {
    private SalesChannelRepository salesChannelRepository;

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
