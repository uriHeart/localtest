package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import com.argo.collect.domain.collector.OrderDetailCollector;
import com.argo.collect.domain.collector.OrderMergeInfo;
import com.argo.collect.domain.enums.WconceptCollect;
import com.argo.common.configuration.ArgoBizException;
import com.argo.common.domain.common.util.ArgoDateUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class WconceptPaymentCompleteCollector extends AbstractWconceptDataCrawler implements OrderDetailCollector {

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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.PAYMENT_COMPLETE.toString());
        Map<String, String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(), cookieMap, requestParam);
        if(doc.getElementById("TreeInxDiv03")==null) return Arrays.asList();

        Element tbody = doc.getElementById("TreeInxDiv03").getElementsByTag("tbody").get(0);

        Flux<String> headerString =
                Flux.fromIterable(tbody.getElementsByTag("tr")
                        .stream()
                        .findFirst()
                        .map(element -> element.getElementsByTag("td")
                                .stream()
                                .map(td -> td.text()))
                        .orElseThrow(new ArgoBizException("wconcept 헤더정보가 없습니다."))
                        .collect(Collectors.toList()))
                ;

        List<List<String>> bodyRows = new ArrayList<>();

        Stack<List<String>> spanData = new Stack<>();
        tbody.getElementsByTag("tr")
                .stream()
                .skip(1L)
                .forEach(tr ->{
                    List<String> rowSpan = new ArrayList<>();
                    AtomicInteger rowSpanSize = new AtomicInteger();

                    Elements tdList = tr.getElementsByTag("td");

                    tdList.stream()
                            .forEach(td ->{
                                if(td.getElementsByAttribute("rowSpan").size()>0) {
                                    rowSpanSize.set(Integer.parseInt(td.attr("rowspan")));
                                    rowSpan.add(td.text());
                                }
                            });

                    for(int i=0; i <rowSpanSize.get(); i++){
                        spanData.push(rowSpan);
                    }

                    List<String> tdDataList =
                            tdList.stream()
                                    .map(td -> td.text())
                                    .collect(Collectors.toList())
                            ;

                    if(tr.getElementsByTag("td").size() < 19){
                        tdDataList.addAll(0,spanData.pop());
                    }

                    bodyRows.add(tdDataList);
                });

        List<Map<String,String>> dataRows =
                bodyRows
                        .stream()
                        .map(tdList ->
                                Flux.fromStream(
                                        tdList
                                                .stream()
                                )
                                        .zipWith(headerString)
                                        .collectMap(Tuple2::getT2,Tuple2::getT1)
                                        .block()
                        )
                        .collect(Collectors.toList())
                ;

        return this.modifyOriginalData(dataRows);
    }

    @Override
    public OrderMergeInfo makeMergeKeyInfo() {
        return OrderMergeInfo
                .builder()
                .eventTypeFieldKey("주문상태")
                .orderIdFieldKey("주문번호")
                .publishedAtFieldKey("결제일자")
                .build();
    }

    @Override
    public List<Map<String, String>> modifyOriginalData(List<Map<String, String>> dataRows) {

        dataRows.forEach(row -> {
            row.put("주문상태", "결제완료");
            row.put("주문일자", row.get("결제일자"));
            row.put("아이템코드","1");
            row.put("판매가",row.get("금액"));

        });

        return dataRows;
    }
}
