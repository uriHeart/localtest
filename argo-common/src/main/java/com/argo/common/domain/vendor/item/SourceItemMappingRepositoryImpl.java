package com.argo.common.domain.vendor.item;

import com.argo.common.configuration.ArgoQueryDslRepositorySupport;
import com.argo.common.domain.channel.QSalesChannel;
import com.argo.common.domain.vendor.QVendor;

import java.util.List;

public class SourceItemMappingRepositoryImpl extends ArgoQueryDslRepositorySupport implements SourceItemMappingRepositoryCustom {

    private QVendor qVendor = QVendor.vendor;
    private QSalesChannel qSalesChannel = QSalesChannel.salesChannel;
    private QSourceItemMapping qSourceItemMapping = QSourceItemMapping.sourceItemMapping;
    private QVendorItem qVendorItem = QVendorItem.vendorItem;

    public SourceItemMappingRepositoryImpl() {
        super(SourceItemMapping.class);
    }

    @Override
    public List<SourceItemMapping> findAllBy() {
        return from(qSourceItemMapping)
                .innerJoin(qSourceItemMapping.vendor, qVendor).fetchJoin()
                .innerJoin(qSourceItemMapping.salesChannel, qSalesChannel).fetchJoin()
                .innerJoin(qSourceItemMapping.vendorItem, qVendorItem).fetchJoin()
                .select(qSourceItemMapping).fetch();
    }
}
