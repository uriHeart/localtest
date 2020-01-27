package com.argo.collect.domain.collector;

import com.argo.common.configuration.ArgoBizException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WconceptPaymentCompleteDataTest {

    @Test
    public void run(){
        File TargetHtml = new File("C:\\project\\arog-server-dev\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\wconceptPaymentComplete.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(TargetHtml,"euc-kr");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element body = doc.body();
        Element tbody = body.getElementById("TreeInxDiv03").getElementsByTag("tbody").get(0);

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

        List<Map<String,String>> bodyData =
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


        bodyData.get(0);
    }
}
