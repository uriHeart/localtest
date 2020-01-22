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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class WconceptDeliveryCollector extends AbstractWconceptDataCrawler implements OrderDetailCollector {

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
        CollectParam collectParam = getCollectingInfo(collectParams).get(WconceptCollect.DELIVERY);
        Map<String,String> requestParam = makeRequestParam(collectParam);

        Document doc = super.getCrawDocument(collectParam.getCollectUrl(),cookieMap,requestParam);

        Element body = doc.body();
        Element tbody = body.getElementById("TreeInxDiv03").getElementsByTag("tbody").get(0);

        List<List<Map<String,String>>> datas =
                tbody.getElementsByTag("tr")
                        .stream()
                        .skip(3)
                        .filter(element -> !element.getElementsByTag("tbody").isEmpty())
                        .map(element -> element.getElementsByTag("tbody").get(0)
                                .getElementsByTag("tr")
                                .stream()
                                .map(tr ->
                                        {
                                            AtomicInteger optionCount = new AtomicInteger(1);

                                            return tr.getElementsByTag("td")
                                                    .stream()
                                                    .filter(td -> !StringUtils.isEmpty(td.attr("title")) )
                                                    .map(td ->{
                                                        if(!td.getElementsByTag("a").isEmpty()){
                                                            Element modifyTd = new Element("td");
                                                            modifyTd.attr("title","상품명");
                                                            modifyTd.text(td.text());
                                                            return  modifyTd;
                                                        }else if(td.attr("style").contains("nowrap")
                                                                && td.getElementsByTag("a").isEmpty()){
                                                            Element modifyTd = new Element("td");
                                                            modifyTd.attr("title","옵션"+optionCount.getAndIncrement());
                                                            modifyTd.text(td.text());
                                                            return  modifyTd;
                                                        }else if("송장번호".equals(td.attr("title"))){
                                                            Element modifyTd = new Element("td");
                                                            modifyTd.attr("title","송장번호");
                                                            modifyTd.text(td.getElementsByTag("input").val());
                                                            return  modifyTd;
                                                        }else{
                                                            return td;
                                                        }

                                                    })
                                                    .collect(Collectors.toMap(element1 -> element1.attr("title"),
                                                            element1 ->element1.text()
                                                    ))
                                                    ;
                                        }
                                ).collect(Collectors.toList())

                        )
                        .collect(Collectors.toList())
                ;

        Map<String,String> orderDateMap =
                tbody.getElementsByTag("tr")
                        .stream()
                        .skip(3)
                        .filter(element -> !element.getElementsByTag("tbody").isEmpty())
                        .map(element -> {

                            String orderDate = element.getElementsByTag("td").get(1).text();
                            String orderKey = element.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text();

                            return orderKey+";"+orderDate;
                        })
                        .collect(Collectors.toMap(s->s.split(";")[0],s -> s.split(";")[1]))
                ;

        List<Map<String,String>> result = new ArrayList<>();


        datas.forEach(dataRow ->{
            dataRow.stream().skip(2).forEach( r ->{
                Map row = new HashMap();
                String orderDate = orderDateMap.get(dataRow.get(0).get("주문번호"));
                dataRow.get(0).put("주문일자",orderDate);
                row.putAll(dataRow.get(0));
                row.putAll(dataRow.get(1));
                row.putAll(r);
                result.add(row);
            });
        });

        return result;
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
