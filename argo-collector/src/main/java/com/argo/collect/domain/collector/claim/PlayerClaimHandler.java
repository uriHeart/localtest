package com.argo.collect.domain.collector.claim;

import java.util.Map;

public interface PlayerClaimHandler {

    boolean isClaim(Map rowData);

    Map makeClaim(Map rowData);

    void modifyOriginalData(Map rowData);

}
