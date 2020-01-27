package com.argo.collect.domain.collector;
import com.argo.common.domain.vendor.VendorChannel;

import java.util.List;
import java.util.Map;

public interface OrderDetailCollector {

    List<Map<String,String>> getCollectDetailData(List<CollectParam> collectParam, Map<String,String> cookieMap);

    OrderMergeInfo makeMergeKeyInfo();

    List<Map<String, String>> modifyOriginalData(List<Map<String, String>> dataRows);

}
