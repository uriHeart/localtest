package com.argo.collect.domain.service;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.excel.ChannelExcelMapping;
import com.argo.collect.domain.excel.ChannelExcelMappingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TestChannelExcelMappingService {

    @Autowired
    ChannelExcelMappingService channelExcelMappingService;

    @Test
    public void getChannelExcelMapping(){
        List<ChannelExcelMapping> result =  channelExcelMappingService.getChannelExcelMapping(new Long(1));
        System.out.println(result);
    }

    @Test
    public void addExcelFactor(){
        List<ChannelExcelMapping> result =  channelExcelMappingService.getChannelExcelMapping(new Long(1));
        System.out.println(result);
    }

}
