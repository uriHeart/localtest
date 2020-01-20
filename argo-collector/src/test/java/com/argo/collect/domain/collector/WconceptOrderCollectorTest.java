package com.argo.collect.domain.collector;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.event.EventConverter;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.common.util.HashUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WconceptOrderCollectorTest extends AbstractOrderCollector {


    @Value("${webdriver.chrome.driver}")
    private String WEB_DRIVER_PATH;

    @Value("${webdriver.id}")
    private String WEB_DRIVER_ID;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private List<EventConverter> eventConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean isSupport(SalesChannel channel) {
        return "W_CONCEPT".equals(channel.getCode());
    }

    @Override
    public void collect(VendorChannel channel) {

        Map<String,String> cookieMap = this.getCookieMap(channel);

        Map<String,String> params = new HashMap<>();
        params.put("flag","submit");
        params.put("vendorcd","3004012");
        params.put("dealtypecd","01");
        params.put("strfrom","2019-12-20");
        params.put("strto","2020-01-20");


        Document document =null;
        try {
             document = Jsoup.connect("http://pin.wconcept.co.kr/ShippingPin/iFrameLstShippingOrderVendorBottom.asp")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")
                    .header("Accept", "text/html, */*; q=0.01")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6")
                    .header("Host", "pin.wconcept.co.kr")
                    .cookies(cookieMap) // 로그인 Cookies 세팅
                    .ignoreContentType(true)
                    .data(params)
                    .post()
                     ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element body = document.body();


    }

    public Map<String,String> getCookieMap(VendorChannel channel){
        AuthorityManager authorityManager = super.getAuth(channel.getSalesChannel().getCode());
        String authorization = authorityManager.requestAuth(channel);
        String[] loginInfo = authorization.split(":");
        String wconceptId = loginInfo[0];
        String wconceptPw = loginInfo[1];

        String baseUrl = channel.getSalesChannel().getBaseUrl();

        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings", true);
//        options.addArguments("headless","window-size=1920x1080","disable-gpu");
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        WebDriver driver = new ChromeDriver(options);

        driver.get(baseUrl);
        driver.switchTo().alert().accept();

        //iframe 내부에서 id 필드 탐색
        WebElement useridElement = driver.findElement(By.id("userid"));
        useridElement.sendKeys(wconceptId);

        //iframe 내부에서 pw 필드 탐색
        WebElement pwElement = driver.findElement(By.id("pw"));
        pwElement.sendKeys(wconceptPw);

        //로그인 버튼 클릭
        driver.findElement(By.className("inputNo")).click();

        Set<Cookie> cookies = driver.manage().getCookies();

        Map<String, String> cookieMap = cookies.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value : " + entry.getValue());
        }
        return cookieMap;
    }

    public HashMap<String, RawEventParam> mergeRawEvent(List<Map> orderList, String eventOrderId, String eventPublishedAt){

        HashMap<String, RawEventParam> mergedOrder = new HashMap<>();
        orderList.forEach(event -> {
            String orderId = String.valueOf(event.get(eventOrderId));
            String publishedAt = String.valueOf(event.get(eventPublishedAt));
            String eventType = String.valueOf(event.get("event_type"));

            String mergeKey = HashUtil.sha256(orderId+publishedAt);

            if (mergedOrder.containsKey(mergeKey)) {
                mergedOrder.get(mergeKey).getDataRows().add(event);
            } else {
                RawEventParam rawEventParam = new RawEventParam();
                rawEventParam.setOrderId(orderId);
                rawEventParam.setPublishedAt(publishedAt);
                rawEventParam.setEventType(eventType);
                rawEventParam.getDataRows().add(event);
                mergedOrder.put(mergeKey, rawEventParam);
            }
        });

        return mergedOrder;
    }

    public void saveRawData(HashMap<String, RawEventParam> mergedOrder, VendorChannel channel) {

        mergedOrder.forEach((key, event) -> {
            String eventToJson = null;

            try {
                eventToJson = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String publishedAt = event.getPublishedAt().replaceAll("\\.", "-");

            if(publishedAt.split(" ").length==1){
                publishedAt += " 00:00:00";
            }

            RawEvent rawEvent = RawEvent.builder()
                    .vendorId(channel.getVendor().getVendorId())
                    .channelId(channel.getSalesChannel().getSalesChannelId())
                    .format("JSON")
                    .auto(true)
                    .data(eventToJson)
                    .orderId(event.getOrderId())
                    .publishedAt(ArgoDateUtil.getDate(publishedAt))
                    .createdAt(new Date())
                    .event(event.getEventType())
                    .build();
            rawEventService.save(rawEvent);
        });
    }


    public List<Map> convertEventType(List<Map> orderList, VendorChannel channel){
        return orderList.stream()
                .map(order->{
                    order.put("event_type",
                            eventConverter.stream()
                                    .filter(converter -> converter.isSupport(channel.getSalesChannel().getCode()))
                                    .map(converter ->converter.getEventType(order))
                                    .findFirst()
                                    .orElse(EventType.OTHER)
                    );
                    return order;
                })
                .collect(Collectors.toList());
    }

    @Test
    public void run(){
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==6){
                collect(channel);
            }
        }
    }
}
