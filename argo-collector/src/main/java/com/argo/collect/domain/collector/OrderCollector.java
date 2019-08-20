package com.argo.collect.domain.collector;

import com.argo.collect.domain.enums.SalesChannel;

public interface OrderCollector {
    public boolean isSupport(SalesChannel channel);
    public void collect(SalesChannel channel);
}
