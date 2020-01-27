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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WconceptReturningAndExchangingDataTest {

    @Test
    public void run(){
        File TargetHtml = new File("C:\\project\\arog-server-dev\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\wconceptReturningAndExchanging.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(TargetHtml,"euc-kr");
        } catch (IOException e) {
            e.printStackTrace();
        }
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


        List<Map<String,String>> basicbody =
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


        basicbody.get(0);


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

        detailBody.get(0);

        List<Map<String,String>> mergedBody =
                Flux.fromIterable(detailBody)
                        .zipWith(Flux.fromIterable(basicbody))
                        .map( t -> {
                            Map<String,String> result = t.getT1();
                            result.putAll(t.getT2());
                            return result;
                        })
                        .collectList()
                        .block()
                ;

        detailBody.get(0);
    }
}
