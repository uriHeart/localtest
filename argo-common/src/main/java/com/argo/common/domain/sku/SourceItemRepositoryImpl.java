package com.argo.common.domain.sku;

import com.argo.common.configuration.ArgoQueryDslRepositorySupport;
import com.argo.common.domain.channel.QSalesChannel;
import com.argo.common.domain.vendor.QVendor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SourceItemRepositoryImpl extends ArgoQueryDslRepositorySupport implements SourceItemRepositoryCustom {
    private QSourceItem qSourceItem = QSourceItem.sourceItem;
    private QVendor qVendor = QVendor.vendor;
    private QSalesChannel qSalesChannel = QSalesChannel.salesChannel;

    public SourceItemRepositoryImpl() {
        super(SourceItem.class);
    }

    @Override
    public List<SourceItem> findAllLazy() {
        return from(qSourceItem)
                .innerJoin(qSourceItem.vendor, qVendor).fetchJoin()
                .innerJoin(qSourceItem.salesChannel, qSalesChannel).fetchJoin()
                .select(qSourceItem).fetch();
    }
}
