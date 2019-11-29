package com.argo.collect.domain.collector.ssg;

import com.argo.collect.domain.collector.AbstractOrderCollector;
import com.argo.common.domain.vendor.VendorChannel;

public class SsgShppDirectionCollector extends AbstractOrderCollector {
    @Override
    public boolean isSupport(String channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel vendorChannel) {

    }
}
