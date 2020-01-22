package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import com.argo.collect.domain.collector.OrderDetailCollector;
import com.argo.collect.domain.collector.OrderMergeInfo;
import com.argo.collect.domain.enums.WconceptCollect;
import com.argo.common.domain.common.util.ArgoDateUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class WconceptReleaseCollector extends AbstractWconceptDataCrawler implements OrderDetailCollector {

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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.RELEASE);
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);
        List<Map<String,String>> basicDataRows =  super.basicDataCollect(doc,"TreeInxDiv03");
        List<Map<String,String>> detailDataRows =  super.basicDataCollect(doc,"TreeInxDiv04");

        List<Map<String,String>> mergedDataRows =
                Flux.fromIterable(basicDataRows)
                        .zipWith(Flux.fromIterable(detailDataRows))
                        .map( t -> {
                            Map<String,String> result = t.getT1();
                            result.putAll(t.getT2());
                            return result;
                        })
                        .collectList()
                        .block()
                        ;

        mergedDataRows.forEach(merge ->{
            String addr =  merge.get("배송지");

            String zipCode =addr.substring(addr.indexOf("[")+1,addr.lastIndexOf("]"));
            merge.put("우편번호",zipCode);
        });

        return mergedDataRows;
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
