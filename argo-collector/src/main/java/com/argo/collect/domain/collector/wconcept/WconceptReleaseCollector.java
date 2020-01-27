package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import com.argo.collect.domain.collector.OrderDetailCollector;
import com.argo.collect.domain.collector.OrderMergeInfo;
import com.argo.collect.domain.enums.WconceptCollect;
import com.argo.common.domain.common.util.ArgoDateUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.RELEASE.toString());
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);
        if(doc.getElementById("TreeInxDiv03")==null) return Arrays.asList();

        Element tbody = doc.getElementById("TreeInxDiv03").getElementsByTag("tbody").get(0);

        Flux<String> headerString =
                Flux.fromIterable(tbody.getElementsByTag("tr")
                        .stream()
                        .limit(2L)
                        .map(element -> element.getElementsByTag("td")
                                .stream()
                                .filter(td -> !StringUtils.isEmpty(td.text()))
                                .map(td -> td.text()))
                        .flatMap(s->s)
                        .collect(Collectors.toList()))
                ;


        List<Map<String,String>> bodyBasic =
                tbody.getElementsByTag("tr")
                        .stream()
                        .skip(2)
                        .map(element ->
                                Flux.fromStream(
                                        element.getElementsByTag("td")
                                                .stream()
                                                .map(td -> td.text()))
                                        .zipWith(headerString)
                                        .collectMap(Tuple2::getT2,Tuple2::getT1)
                                        .block()
                        )
                        .collect(Collectors.toList())
                ;

        Element tbody2 = doc.getElementById("TreeInxDiv04").getElementsByTag("tbody").get(0);

        Flux<String> headerString2 =
                Flux.fromIterable(tbody2.getElementsByTag("tr")
                        .stream()
                        .skip(1)
                        .limit(2L)
                        .map(element -> element.getElementsByTag("td")
                                .stream()
                                .filter(td -> !StringUtils.isEmpty(td.text()))
                                .map(td -> td.text()))
                        .flatMap(s->s)
                        .collect(Collectors.toList()))
                ;


        List<Map<String,String>> bodyDetail =
                tbody2.getElementsByTag("tr")
                        .stream()
                        .skip(2)
                        .map(element ->
                                Flux.fromStream(
                                        element.getElementsByTag("td")
                                                .stream()
                                                .map(td -> td.text()))
                                        .zipWith(headerString2)
                                        .collectMap(Tuple2::getT2,Tuple2::getT1)
                                        .block()
                        )
                        .collect(Collectors.toList())
                ;

        List<Map<String,String>> mergedDataRows =
                Flux.fromIterable(bodyDetail)
                        .zipWith(Flux.fromIterable(bodyBasic))
                        .map( t -> {
                            Map<String,String> result = t.getT1();
                            result.putAll(t.getT2());
                            return result;
                        })
                        .collectList()
                        .block()
                ;


        return this.modifyOriginalData(mergedDataRows);
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
            String addr =  row.get("배송지");

            String zipCode =addr.substring(addr.indexOf("[")+1,addr.lastIndexOf("]"));
            row.put("우편번호",zipCode);
            row.put("주문상태","상품준비중");

            //결제일자 +1 시간
            Date payDate = ArgoDateUtil.getDateByTimeFormatHH(row.get("결제일자 (교환출고지시)"));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(payDate.toInstant(),ZoneId.systemDefault());
            LocalDateTime eventDateTime = localDateTime.plusHours(1L);
            String releaseDate = eventDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            row.put("publishedAt",releaseDate);
            row.put("주문일자",row.get("결제일자 (교환출고지시)"));
            row.put("아이템코드","1");

        });

        return dataRows;
    }
}
