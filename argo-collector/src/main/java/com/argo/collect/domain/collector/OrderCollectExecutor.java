package com.argo.collect.domain.collector;

import com.argo.collect.domain.enums.SalesChannel;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderCollectExecutor {

    @Autowired
    private List<OrderCollector> collectors;

    @Autowired
    private VendorService vendorService;

    private boolean collectRun = false;

    @Scheduled(cron = "0 0/5 * * * *")
    public void run() {
        if (isRun()) {
            log.info("Already Order Collecting ##### ");
            return;
        }
        try {
            setRun(true);
            for (VendorChannel channel : vendorService.autoCollectingTargets()) {
                collectors.forEach(
                        c -> {
                            if (c.isSupport(channel.getSalesChannel().getCode())) {
                                c.collect(channel);
                            }
                        });
            }
        } finally {
            setRun(false);
        }
    }

    public synchronized boolean isRun() {
        return collectRun;
    }

    public synchronized void setRun(boolean value) {
        this.collectRun = value;
    }
}
