package com.argo.collect.domain.collector;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//@SpringBootTest
public class SeleniumTest {

    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\project\\argo_server_prd\\argo-collector\\src\\main\\resources\\driver\\chrome\\chromedriver.exe";


    @Test
    public void sel() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings", true);
//        options.addArguments("headless","window-size=1920x1080","disable-gpu");
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        driver = new ChromeDriver(options);


        driver.get("https://pin.wconcept.co.kr/");
        driver.switchTo().alert().accept();

        //iframe 내부에서 id 필드 탐색
        WebElement useridElement = driver.findElement(By.id("userid"));
        String userId ="goal";
        useridElement.sendKeys(userId);

        //iframe 내부에서 pw 필드 탐색
        WebElement pwElement = driver.findElement(By.id("pw"));
        String pass ="wagti2019!@";
        pwElement.sendKeys(pass);

        //로그인 버튼 클릭
        driver.findElement(By.className("inputNo")).click();
        Thread.sleep(2000);
        try {
            //비밀번호 교체알림
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
        Thread.sleep(3000);
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
            Map<String, String> rowData = new HashMap<>();
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
            Map<String, String> rowData = new HashMap<>();
            AtomicInteger headerCount = new AtomicInteger();
            tdList.forEach( td ->{
                rowData.put(addrHeaderData.get(headerCount.get()),td.getText());
                headerCount.getAndIncrement();
            });
            addrList.add(rowData);
        }


        for(int i=0; i < prodList.size(); i++){
            prodList.get(i).putAll(addrList.get(i));
        }

        //prodList.get(0).get("결제일자\n(교환출고지시)");
        //prodList.get(0).get("주문번호")

        driver.switchTo().parentFrame();
        //전체주문
        driver.findElement(By.xpath("//*[@id=\"Layermenu_2\"]/table/tbody/tr[5]/td[3]/a")).click();

        //데이터 조회 대기
        Thread.sleep(3000);
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

            Map<String, String> rowData = new HashMap<>();
            AtomicInteger headerCount = new AtomicInteger();
            dtList.forEach( td ->{
                rowData.put(headerData.get(headerCount.get()),td.getText());
                headerCount.getAndIncrement();
            });
            orderList.add(rowData);

        });

        System.out.println(orderList.size());

    }
}
