package com.argo.collect.domain.collector;

import com.argo.collect.domain.enums.SalesChannel;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCollectExecutor {

    @Autowired
    private List<OrderCollector> collectors;

    @Autowired
    private VendorService vendorService;

    @Scheduled(cron = "0 0/5 * * * *")
    public void run() {

        for (VendorChannel channel : vendorService.listAllByEnabled()) {
            collectors.forEach(
                c -> {
                    if (c.isSupport(SalesChannel.valueOf(channel.getSalesChannel().getCode()))) {
                        c.collect(channel);
                    }
                });
        }
    }
}
