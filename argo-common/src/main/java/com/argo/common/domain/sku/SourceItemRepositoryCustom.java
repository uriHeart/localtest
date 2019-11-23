package com.argo.common.domain.sku;

import java.util.List;

public interface SourceItemRepositoryCustom {
    List<SourceItem> findAllLazy();
}
