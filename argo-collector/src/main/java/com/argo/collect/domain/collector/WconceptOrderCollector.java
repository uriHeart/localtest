package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.event.EventConverter;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.common.util.HashUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventRepository;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Slf4j
@Service
public class WconceptOrderCollector extends AbstractOrderCollector{


    @Value("${webdriver.chrome.driver}")
    private String WEB_DRIVER_PATH;

    @Value("${webdriver.id}")
    private String WEB_DRIVER_ID;

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
        try {
            //비밀번호변경확인
            Thread.sleep(2000);
            driver.findElement(By.className("close_btn")).click();
        }catch (Exception e){
            System.out.println("팝업창 없음으로 패스함");
        }
        try {
            //공지사항
            driver.findElement(By.className("close_btn")).click();
        }catch (Exception e){
            System.out.println("팝업창 없음으로 패스함");
        }
        driver.findElement(By.id("title_2")).click();
        //상품준비중
        //배송관리 클릭
        driver.findElement(By.xpath("//*[@id=\"Layermenu_2\"]/table/tbody/tr[1]/td[3]")).click();
        //상품준비중 클릭
        driver.findElement(By.xpath("//*[@id=\"middle_9\"]/tbody/tr[3]/td[3]")).click();

        //데이터 조회 대기
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.switchTo().frame("iframebottom");
        int prodTrSize = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv03\"]/table/tbody/tr")).size();

        List<Map> prodList = new ArrayList<>();
        List<String> prodHeaderData = new ArrayList<>();

        List<WebElement> prodHeaderList = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv03\"]/table/tbody/tr[2]/td"));

        prodHeaderList.forEach(header -> {
            prodHeaderData.add(header.getText());
        });


        for(int i=3; i <= prodTrSize; i++ ){
            List<WebElement> tdList = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv03\"]/table/tbody/tr["+i+"]/td"));
            Map<String,String> rowData = new HashMap<>();
            AtomicInteger headerCount = new AtomicInteger();
            tdList.stream().skip(1).forEach( td ->{
                rowData.put(prodHeaderData.get(headerCount.get()),td.getText());
                headerCount.getAndIncrement();
            });
            prodList.add(rowData);
        }


        int addrTrSize = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv04\"]/table/tbody/tr")).size();

        List<Map> addrList = new ArrayList<>();
        List<String> addrHeaderData = new ArrayList<>();

        List<WebElement> addrHeaderList = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv04\"]/table/tbody/tr[2]/td"));

        addrHeaderList.forEach(header -> {
            addrHeaderData.add(header.getText());
        });

        if(!addrHeaderData.isEmpty()) addrHeaderData.add(21,"개인통관부호");

        for(int i=3; i <= addrTrSize; i++ ){
            List<WebElement> tdList = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv04\"]/table/tbody/tr["+i+"]/td"));
            Map<String,String> rowData = new HashMap<>();
            AtomicInteger headerCount = new AtomicInteger();
            tdList.forEach( td ->{
                rowData.put(addrHeaderData.get(headerCount.get()),td.getText());
                headerCount.getAndIncrement();
            });
            rowData.put("주문상태","상품준비중");
            addrList.add(rowData);
        }

        for(int i=0; i < prodList.size(); i++){
            prodList.get(i).putAll(addrList.get(i));
        }


        //prodList.get(0).get("결제일자\n(교환출고지시)");
        //prodList.get(0).get("주문번호")
        this.convertEventType(prodList,channel);
        HashMap<String, RawEventParam> mergedOrder = this.mergeRawEvent(prodList,"주문번호","결제일자\n(교환출고지시)");

        this.saveRawData(mergedOrder,channel);


        driver.switchTo().parentFrame();
        //*[@id="Layermenu_2"]/table/tbody/tr[5]/td[3]
        //전체주문 //*[@id="Layermenu_2"]/table/tbody/tr[5]/td[3]
        driver.findElement(By.xpath("//*[@id=\"Layermenu_2\"]/table/tbody/tr[5]/td[3]/a")).click();

        //데이터 조회 대기
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.switchTo().frame("iframebottom");

        //데이터 조회 테이블
        List<WebElement> trList = driver.findElements(By.xpath("//*[@id=\"TreeInxDiv03\"]/table/tbody")).get(0).findElements(By.tagName("tr"));

        List<String> headerData = new ArrayList<>();
        List<Map> orderList = new ArrayList<>();

        trList
                .stream()
                .findFirst()
                .map(tr -> {
                    List<WebElement> dtList = tr.findElements(By.tagName("td"));
                    dtList.forEach(td->{
                        headerData.add(td.getText());
                        System.out.println(td.getText());
                    });
                    return tr;
                })
        ;

        trList.stream().skip(1).forEach(tr -> {

            List<WebElement> dtList = tr.findElements(By.tagName("td"));

            Map<String,String> rowData = new HashMap<>();
            AtomicInteger headerCount = new AtomicInteger();
            dtList.forEach( td ->{
                rowData.put(headerData.get(headerCount.get()),td.getText());
                headerCount.getAndIncrement();
            });
            orderList.add(rowData);

        });

        this.convertEventType(orderList,channel);
        HashMap<String, RawEventParam> mergedAllOrder = this.mergeRawEvent(orderList,"주문번호","주문일자");
        this.saveRawData(mergedAllOrder,channel);

    }

    public HashMap<String, RawEventParam> mergeRawEvent(List<Map> orderList,String eventOrderId,String eventPublishedAt){

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


    public List<Map> convertEventType(List<Map> orderList,VendorChannel channel){
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
}
