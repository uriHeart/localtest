package com.argo.collect.domain.collector;

import com.argo.collect.domain.enums.SalesChannel;
import com.argo.common.domain.vendor.VendorChannel;

public interface OrderCollector {
    public boolean isSupport(String channel);
    public void collect(VendorChannel vendorChannel);
}
