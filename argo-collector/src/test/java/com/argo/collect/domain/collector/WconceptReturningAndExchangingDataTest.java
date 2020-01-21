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
import java.util.stream.Collectors;

public class WconceptReturningAndExchangingDataTest {

    @Test
    public void run(){
        File TargetHtml = new File("C:\\project\\argo_server_dev\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\wconceptReturn.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(TargetHtml,"UTF-8");
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


        List<Map<String,String>> bodyString =
                tbody.getElementsByTag("tr")
                    .stream()
                    .skip(1)
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



        bodyString.forEach(row ->{
            String[] options = row.get("옵션1").split(" ");
            if(!StringUtils.isEmpty(options) && options.length>1){
                row.put("교환전옵션",options[0]);
                row.put("교환후옵션",options[1]);
            }
        });
        bodyString.get(0);
    }
}
