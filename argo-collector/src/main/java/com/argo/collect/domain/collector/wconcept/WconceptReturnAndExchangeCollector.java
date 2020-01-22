package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import com.argo.collect.domain.collector.OrderDetailCollector;
import com.argo.collect.domain.collector.OrderMergeInfo;
import com.argo.collect.domain.enums.WconceptCollect;
import com.argo.common.domain.common.util.ArgoDateUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class WconceptReturnAndExchangeCollector extends AbstractWconceptDataCrawler implements OrderDetailCollector {

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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.RETURN_AND_EXCHANGE);
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);

        List<Map<String,String>> dataRows = super.basicDataCollect(doc,"TreeInxDiv03");

        dataRows.forEach(row ->{
                    String[] options = row.get("옵션1").split(" ");
                    if(!StringUtils.isEmpty(options) && options.length>1){
                        row.put("교환전옵션",options[0]);
                        row.put("교환후옵션",options[1]);
                    }
                });

        return dataRows;
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
