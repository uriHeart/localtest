package com.argo.collect.domain.collector.wconcept;

import com.argo.common.configuration.ArgoBizException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractWconceptDataCrawler implements OrderCollectInfoMapper{

    Document getCrawDocument(String url, Map cookieMap, Map paramMap){

        Document document =null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")
                    .header("Accept", "text/html, */*; q=0.01")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6")
                    .header("Host", "pin.wconcept.co.kr")
                    .cookies(cookieMap) // 로그인 Cookies 세팅
                    .ignoreContentType(true)
                    .data(paramMap)
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ArgoBizException("wconcept 정보조회에 실패했습니다 : "+url, e);
        }

        return document;
    }

    List<Map<String,String>> basicDataCollect(Element body,String collectDiv){
        if(body.getElementById(collectDiv)==null) return Arrays.asList();
        Element tbody = body.getElementById(collectDiv).getElementsByTag("tbody").get(0);

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

        return tbody.getElementsByTag("tr")
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
    }

}
