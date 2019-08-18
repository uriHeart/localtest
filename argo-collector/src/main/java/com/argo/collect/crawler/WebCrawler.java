package com.argo.collect.crawler;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class WebCrawler {
    Function<Long, String> convertVendorNumber = (l) -> l.toString().replaceFirst("(\\d{3})(\\d{2})(\\d+)", "$1-$2-$3");

    @PostConstruct
    private void init() throws IOException {
        System.setProperty("webdriver.chrome.driver", "/Users/austin/Downloads/chromedriver");
    }

    public void vendorCrawling(String vendorCode, String saleChannelCode) throws IOException, InterruptedException {
        //TODO 데이터 조회 후 변경 필요
        VendorSaleChannelInfo info = this.getVendorSaleChannelInfo(vendorCode, saleChannelCode);
        List<WebCrawlerSourceTree> roots = this.getWebCrawlerSourceTreesForVendor(info);

        for(WebCrawlerSourceTree root : roots) {
            boolean result = this.crawling(root);

            if(info.isFindAll() && result) {
                return;
            }
        }
    }

    private Document getDocument(WebCrawlerSourceTree root) throws IOException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(root.getFullUrl());

            for (int second = 0;; second++) {
                if(second >=root.getLoadSecond()){
                    break;
                }

                Actions actions = new Actions(driver);
                actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
                Thread.sleep(1000);
            }

            return Jsoup.parse(driver.getPageSource());
        } finally {
            driver.close();
        }
    }

    private boolean crawling(WebCrawlerSourceTree root) throws IOException, InterruptedException {
        Document doc = this.getDocument(root);

        if(!this.validateDoc(doc, root.getValidateMap())) {
            return false;
        }

        boolean result = false;

        //TODO 리스트인 페이지에 대해 모든 리스트 url 추출 필요

        for(Element element : root.getExtractTags().stream().map(doc::select).flatMap(Elements::stream).collect(Collectors.toList())) {
            if(root.isLeaf()) {
                String html = element.html();
                log.info(html);
                //TODO html을 저장한다.
                return true;
            }

            for (WebCrawlerSourceTree template : root.getChildNodes()) {
                String childPathWithQueryString = Stream.of(template.getPath(), template.getUrlPath())
                        .filter(Objects::nonNull)
                        .map(path -> element.getElementsByAttributeValueContaining("href", path))
                        .flatMap(Elements::stream)
                        .map(e -> e.attr("href"))
                        .findFirst()
                        .map(childUrl -> {
                            int index = childUrl.indexOf(template.getUrl());
                            return index == -1 ? childUrl : childUrl.substring(childUrl.indexOf(template.getUrl()) + template.getUrl().length());
                        })
                        .orElse(null);

                if(Objects.nonNull(childPathWithQueryString)) {
                    String[] split = childPathWithQueryString.split("\\?");

                    String path = split[0];
                    Map<String, String> convertedQueryStrings = Maps.newHashMap();

                    if(split.length > 1) {
                        for(Map.Entry<String, String> queryString : Splitter.on('&').trimResults().withKeyValueSeparator('=').split(split[1]).entrySet()) {
                            String convertedKey = URLDecoder.decode(queryString.getKey(), template.getCharset());
                            String convertedValue = URLDecoder.decode(queryString.getValue(), template.getCharset());

                            convertedQueryStrings.put(convertedKey, convertedValue);
                        }
                    }

                    result = this.crawling(template.create(path, convertedQueryStrings));

                    if(!root.isFindAll() && result) {
                        return true;
                    }
                }
            }
        }

        return result;
    }

    private boolean validateDoc(Document doc, Map<String, Set<String>> validateMap) {
        if(Objects.isNull(validateMap)) {
            return true;
        }

        return validateMap.entrySet().stream()
                .map(entry -> Pair.of(doc.select(entry.getKey()).html(), entry.getValue()))
                .allMatch(pair -> pair.getValue().stream().allMatch(pair.getKey()::contains));

    }

    /******************* 서비스 필요 *******************/

    private VendorSaleChannelInfo getVendorSaleChannelInfo(String vendorCode, String saleChannelCode) {
        //get vendor info
        String vendorName = "큐인사이트";
        long businessNumber = 4771600628L;

        //get vendor channel keywords
        Map<String, Set<String>> channelKeywords = Maps.newHashMap();
        channelKeywords.put("COUPANG", Collections.singleton("초고속 USB 3.1 20/60/120cm TF/SD카드리더 슬림 허브"));
        channelKeywords.put("INTERPARK", Collections.singleton(vendorName));
        channelKeywords.put("AUCTION", Collections.singleton(vendorName));

        //get channel tree
        Set<String> keywords = channelKeywords.get(saleChannelCode);

        return VendorSaleChannelInfo.builder()
                .vendorCode(vendorCode)
                .saleChannelCode(saleChannelCode)
                .vendorName(vendorName)
                .businessNumber(businessNumber)
                .keywords(keywords)
                .findAll(false)
                .build();
    }

    private List<WebCrawlerSourceTree> getWebCrawlerSourceTreesForVendor(VendorSaleChannelInfo info) {
        switch (info.getSaleChannelCode()) {
            case "COUPANG" :
                return this.getCoupangSource(info);
            case "INTERPARK" :
                return this.getInterpark(info);
            case "AUCTION" :
                return this.getAuction(info);
        }
        return null;
    }

    private List<WebCrawlerSourceTree> getCoupangSource(VendorSaleChannelInfo info) {
        List<WebCrawlerSourceTree> roots = info.getKeywords().stream()
                .map(keyword ->
                        WebCrawlerSourceTree.builder()
                                .url("www.coupang.com")
                                .path("/np/search")
                                .extractTags(Sets.newHashSet(".search-product"))
                                .findAll(false)
                                .queryStrings(Collections.singletonMap("q", keyword)) //
                                .build()
                ).collect(Collectors.toList());

        WebCrawlerSourceTree branch1 = WebCrawlerSourceTree.builder()
                .url("www.coupang.com")
                .path("/vp/products")
                .extractTags(Sets.newHashSet(".prod-vendor-container"))
                .findAll(false)
                .validateMap(Collections.singletonMap(
                        ".product-seller",
                        Sets.newHashSet(info.getVendorName(), convertVendorNumber.apply(info.getBusinessNumber()))))
                .build();

        WebCrawlerSourceTree branch2 = WebCrawlerSourceTree.builder()
                .url("www.coupang.com")
                .path("/vp/vendors")
                .extractTags(Sets.newHashSet(".scp-list-area__list"))
                .findAll(true)
                .build();

        WebCrawlerSourceTree leaf = WebCrawlerSourceTree.builder()
                .url("www.coupang.com")
                .path("/vp/products")
                .extractTags(Sets.newHashSet(".contents"))
                .findAll(false)
                .validateMap(Collections.singletonMap(
                        ".product-seller",
                        Sets.newHashSet(info.getVendorName(), convertVendorNumber.apply(info.getBusinessNumber())))) //seller check
                .build();

        roots.forEach(branch1::setParentNode);
        branch2.setParentNode(branch1);
        leaf.setParentNode(branch2);

        return roots;
    }

    private List<WebCrawlerSourceTree> getInterpark(VendorSaleChannelInfo info) {
        List<WebCrawlerSourceTree> roots = info.getKeywords().stream()
                .map(keyword ->
                        WebCrawlerSourceTree.builder()
                                .url("shopping.interpark.com")
                                .path("/shopSearch.do")
                                .extractTags(Sets.newHashSet("#normalList .goods"))
                                .findAll(false)
                                .queryStrings(Collections.singletonMap("q", keyword))
                                .build()
                )
                .collect(Collectors.toList());

        //FIXME 추출 시 리스트의 값이 없는 문제 확인 필요(ASP 문제인듯?)
        WebCrawlerSourceTree branch1 = WebCrawlerSourceTree.builder()
                .url("www.interpark.com")
                .path("/display/sellerAllProduct.do")
                .extractTags(Sets.newHashSet("#goods_list_table tr"))
                .findAll(true)
                .validateMap(Collections.singletonMap(
                        ".layer_seller_info",
                        Sets.newHashSet(info.getVendorName(), convertVendorNumber.apply(info.getBusinessNumber()))))
                .build();

        WebCrawlerSourceTree leaf = WebCrawlerSourceTree.builder()
                .url("www.interpark.com")
                .path("/product/MallDisplay.do")
                .extractTags(Sets.newHashSet(".contents"))
                .findAll(true)
                .validateMap(Collections.singletonMap(
                        ".sellerArea",
                        Sets.newHashSet(info.getVendorName(), info.getBusinessNumber().toString())))
                .build();

        roots.forEach(branch1::setParentNode);
        leaf.setParentNode(branch1);

        return roots;
    }

    private List<WebCrawlerSourceTree> getAuction(VendorSaleChannelInfo info) {
        List<WebCrawlerSourceTree> roots = info.getKeywords().stream()
                .map(keyword ->
                        WebCrawlerSourceTree.builder()
                                .url("browse.auction.co.kr")
                                .path("/search")
                                .extractTags(Sets.newHashSet(".section--itemcard_info"))
                                .findAll(false)
                                .queryStrings(Collections.singletonMap("keyword", keyword))
                                .build()
                ).collect(Collectors.toList());

        WebCrawlerSourceTree branch1 = WebCrawlerSourceTree.builder()
                .url("stores.auction.co.kr")
                .extractTags(Sets.newHashSet(".prd_info")) //
                .validateMap(Collections.singletonMap(
                        ".seller_info_box",
                        Sets.newHashSet(info.getVendorName(), info.getBusinessNumber().toString())))
                .findAll(true)
                .build();

        WebCrawlerSourceTree leaf = WebCrawlerSourceTree.builder()
                .url("itempage3.auction.co.kr")
                .path("/DetailView.aspx")
                .extractTags(Sets.newHashSet("#content"))
                .build();

        roots.forEach(branch1::setParentNode);
        leaf.setParentNode(branch1);

        return roots;
    }

}
