package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import com.argo.collect.domain.collector.OrderDetailCollector;
import com.argo.collect.domain.collector.OrderMergeInfo;
import com.argo.collect.domain.enums.WconceptCollect;
import com.argo.common.domain.common.util.ArgoDateUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class WconceptCancelCollector extends AbstractWconceptDataCrawler implements OrderDetailCollector {

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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.CANCEL.toString());
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);

        List<Map<String, String>> dataRows = super.basicDataCollect(doc, "TreeInxDiv03");
        return this.modifyOriginalData(dataRows);
    }

    @Override
    public OrderMergeInfo makeMergeKeyInfo() {
        return OrderMergeInfo
                .builder()
                .eventTypeFieldKey("주문상태")
                .orderIdFieldKey("주문번호")
                .publishedAtFieldKey("publishedAt")
                .build();
    }

    @Override
    public List<Map<String, String>> modifyOriginalData(List<Map<String, String>> dataRows) {

        dataRows.forEach(row ->{

            Date payDate = ArgoDateUtil.parseDateString(row.get("취소일"));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(payDate.toInstant(), ZoneId.systemDefault());
            LocalDateTime eventDateTime = localDateTime.plusHours(23L).plusMinutes(59L).plusSeconds(59L);
            String cancelCompleteDate = eventDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            row.put("publishedAt",cancelCompleteDate);
            row.put("주문일자",row.get("결제일자"));
            row.put("판매가",row.get("금액"));

        });
        return dataRows;
    }
}
