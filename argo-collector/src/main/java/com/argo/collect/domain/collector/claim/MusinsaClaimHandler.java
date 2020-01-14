package com.argo.collect.domain.collector.claim;

import java.util.Map;

public interface MusinsaClaimHandler {

    boolean isClaim(Map rowData);

    Map makeClaim(Map rowData);

    public void modifyOriginalData(Map rowData);

}
