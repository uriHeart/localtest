package com.argo.collect.service;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.channel.SalesChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommonCodeService {

    @Autowired
    SalesChannelService salesChannelService;

    public List<SalesChannel> getChannel(){
        return salesChannelService.listAll();
    }
}
