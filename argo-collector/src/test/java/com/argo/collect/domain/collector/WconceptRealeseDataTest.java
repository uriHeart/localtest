package com.argo.collect.domain.collector;

import com.argo.common.configuration.ArgoBizException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class WconceptRealeseDataTest {

    @Test
    public void run(){
        File TargetHtml = new File("C:\\project\\argo_server_dev\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\wconceptRelease.html");
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
                        .limit(2L)
                        .map(element -> element.getElementsByTag("td")
                                .stream()
                                .filter(td -> !StringUtils.isEmpty(td.text()))
                                .map(td -> td.text()))
                        .flatMap(s->s)
                        .collect(Collectors.toList()))
                        ;


        List<Map<String,String>> bodyString =
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


        bodyString.get(0);


        Element tbody2 = body.getElementById("TreeInxDiv04").getElementsByTag("tbody").get(0);

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


        List<Map<String,String>> bodyString2 =
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

        bodyString2.get(0);

        List<Map<String,String>> results =
                            Flux.fromIterable(bodyString2)
                            .zipWith(Flux.fromIterable(bodyString))
                            .map( t -> {
                               Map<String,String> result = t.getT1();
                               result.putAll(t.getT2());
                               return result;
                            })
                            .collectList()
                            .block()
                            ;

        results.forEach(r ->{
            String addr =  r.get("배송지");

            String zipCode =addr.substring(addr.indexOf("[")+1,addr.lastIndexOf("]"));
            r.put("우편번호",zipCode);
        });

        results.get(0);
    }
}
