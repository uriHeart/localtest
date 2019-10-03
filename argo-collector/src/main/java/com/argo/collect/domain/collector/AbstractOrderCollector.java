package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.enums.SalesChannel;
import com.argo.common.domain.channel.ChannelCollectInfo;
import com.argo.common.domain.channel.SalesChannelService;
import com.argo.common.domain.raw.RawEventService;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractOrderCollector implements OrderCollector {
    @Autowired
    private List<AuthorityManager> authorityManagers;

    @Autowired
    protected RawEventService rawEventService;

    @Autowired
    private SalesChannelService salesChannelService;

    protected AuthorityManager getAuth(String channel) {
        return authorityManagers.stream().filter(a -> a.isTargetChannel(channel)).findFirst().orElse(null);
    }

    protected CollectParam getCollectInfo(VendorChannel channel) {
        ObjectMapper objectMapper = new ObjectMapper();
        ChannelCollectInfo info = salesChannelService.getChannelCollectInfo(channel.getSalesChannel());
        try {
            return CollectParam.builder()
                    .collectUrl(channel.getSalesChannel().getBaseUrl() + info.getCollectUri())
                    .collectParam(objectMapper.readValue(info.getCollectParam(), Map.class))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
