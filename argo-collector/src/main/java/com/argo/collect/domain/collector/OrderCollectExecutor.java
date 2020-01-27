package com.argo.collect.domain.collector;

import com.argo.common.domain.notification.Notifier;
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

    @Autowired
    private Notifier notifier;

    private boolean collectRun = false;

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        if (isRun()) {
            log.info("Already Order Collecting ##### ");
            return;
        }
        try {
            setRun(true);
            for (VendorChannel channel : vendorService.autoCollectingTargets()) {
                if(channel.getVendorChannelId()==6L) {
                    collectors.forEach(
                            c -> {
                                if (c.isSupport(channel.getSalesChannel())) {
                                    c.collect(channel);
                                }
                            });
                }
            }
        } catch (Exception e) {
            notifier.send(e);
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
