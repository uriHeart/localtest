package com.argo.api.domain.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelExcelMappingService {

    @Autowired
    ChannelExcelMappingRepository channelExcelMappingRepository;

    public List<ChannelExcelMapping> getChannelExcelMapping(Long salesChannelId){
        return channelExcelMappingRepository.findBySalesChannelId(salesChannelId);

    }
}
