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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.RETURN_AND_EXCHANGE.toString());
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);
        if(doc.getElementById("TreeInxDiv03")==null) return Arrays.asList();

        Element body = doc.body();
        Element tbody = body.getElementById("TreeInxDiv03").getElementsByTag("tbody").get(0);


        Flux<String> basicHeader =
                Flux.fromIterable(tbody.getElementsByTag("tr")
                        .stream()
                        .limit(2L)
                        .skip(1L)
                        .map(element -> element.getElementsByTag("td")
                                .stream()
                                .filter(td -> !StringUtils.isEmpty(td.text()))
                                .map(td -> td.text()))
                        .flatMap(s->s)
                        .collect(Collectors.toList()))
                ;

        List<Map<String,String>> basicBody =
                tbody.getElementsByTag("tr")
                        .stream()
                        .skip(2)
                        .map(element ->
                                Flux.fromStream(
                                        element.getElementsByTag("td")
                                                .stream()
                                                .filter(td -> !StringUtils.isEmpty(td.text()))
                                                .map(td -> td.text()))
                                        .zipWith(basicHeader)
                                        .collectMap(Tuple2::getT2,Tuple2::getT1)
                                        .block()
                        )
                        .collect(Collectors.toList())
                ;


        Element tbody2 = body.getElementById("TreeInxDiv04").getElementsByTag("tbody").get(0);


        List<String> detailHeader = tbody2.getElementsByTag("tr")
                .stream()
                .limit(2L)
                .skip(1)
                .map(element -> element.getElementsByTag("td")
                        .stream()
                        .filter(td -> !StringUtils.isEmpty(td.text()))
                        .map(td -> td.text()))
                .flatMap(s->s)
                .collect(Collectors.toList());
        detailHeader.addAll(6, Arrays.asList("회수관리메모","반품사유","반품내용","첨부파일"))
        ;

        List<Map<String,String>> detailBody =
                tbody2.getElementsByTag("tr")
                        .stream()
                        .skip(2)
                        .map(element ->
                                Flux.fromStream(
                                        element.getElementsByTag("td")
                                                .stream()
                                                .map(td -> td.text()))
                                        .zipWith(Flux.fromIterable(detailHeader))
                                        .collectMap(Tuple2::getT2,Tuple2::getT1)
                                        .block()
                        )
                        .collect(Collectors.toList())
                ;

        List<Map<String,String>> mergedBody =
                Flux.fromIterable(detailBody)
                        .zipWith(Flux.fromIterable(basicBody))
                        .map( t -> {
                            Map<String,String> result = t.getT1();
                            result.putAll(t.getT2());
                            return result;
                        })
                        .collectList()
                        .block()
                ;

        return this.modifyOriginalData(mergedBody);
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

            String[] options = row.get("옵션1").split(" ");
            if(!StringUtils.isEmpty(options) && options.length>1){
                row.put("교환전옵션",options[0]);
                row.put("교환후옵션",options[1]);
            }

            Date payDate = ArgoDateUtil.parseDateString(row.get("요청일자"));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(payDate.toInstant(), ZoneId.systemDefault());
            LocalDateTime eventDateTime = localDateTime.plusSeconds(3L);
            String date = eventDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            row.put("publishedAt",date);
            row.put("주문일자",row.get("결제일자"));
            row.put("판매가",row.get("판매가(합계)"));
            row.put("아이템코드","1");



        });
        return dataRows;
    }
}
