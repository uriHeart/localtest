package com.argo.common.domain.channel;

import com.argo.common.domain.vendor.VendorChannelService;
import com.argo.common.domain.vendor.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesChannelService {
    @Autowired
    private SalesChannelRepository salesChannelRepository;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ChannelCollectInfoRepository channelCollectInfoRepository;

    public List<SalesChannel> listAll() {
        return salesChannelRepository.findAll();
    }

    public ChannelCollectInfo getChannelCollectInfo(SalesChannel salesChannel) {
        return channelCollectInfoRepository.findBySalesChannel(salesChannel);
    }

    public SalesChannel getSalesChannel(Long salesChannelId) {
        return salesChannelRepository.findBySalesChannelId(salesChannelId);
    }
}
