package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import com.argo.collect.domain.collector.OrderDetailCollector;
import com.argo.collect.domain.collector.OrderMergeInfo;
import com.argo.collect.domain.enums.WconceptCollect;
import com.argo.common.domain.common.util.ArgoDateUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class WconceptExchangeCollector extends AbstractWconceptDataCrawler implements OrderDetailCollector {

    @Override
    public Map<String, String> makeRequestParam(CollectParam collectParam) {

        Map<String,String> params = collectParam.getCollectParam();
        params.remove("collectType");
        params.put("strfrom", ArgoDateUtil.getDateString(LocalDate.now().minusWeeks(1L)));
        params.put("strto", ArgoDateUtil.getDateString(LocalDate.now()));

        return params;
    }


    @Override
    public List<Map<String,String>> getCollectDetailData(List<CollectParam> collectParams, Map<String, String> cookieMap) {
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.EXCHANGE);
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);

        return super.basicDataCollect(doc,"TreeInxDiv03");
    }

    @Override
    public OrderMergeInfo makeMergeKeyInfo() {
        return OrderMergeInfo
                .builder()
                .eventTypeFieldKey("요청유형")
                .orderIdFieldKey("주문번호")
                .publishedAtFieldKey("주문일자")
                .build();
    }
}
