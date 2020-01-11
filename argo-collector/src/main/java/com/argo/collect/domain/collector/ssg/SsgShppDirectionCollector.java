package com.argo.collect.domain.collector.ssg;

import com.argo.collect.domain.collector.AbstractOrderCollector;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.vendor.VendorChannel;

public class SsgShppDirectionCollector extends AbstractOrderCollector {
    @Override
    public boolean isSupport(SalesChannel channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel vendorChannel) {
    }
}
