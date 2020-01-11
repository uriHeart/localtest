package com.argo.collect.domain.collector;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.collector.ssg.SsgRawEventParam;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SsgOrderCollectorTest extends AbstractOrderCollector {

    @Autowired
    private VendorService vendorService;

    @Override
    public boolean isSupport(SalesChannel channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel vendorChannel) {
        List<CollectParam> collectParam = getCollectInfoList(vendorChannel);

        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        String token = "03e5cca2-08dc-4a37-9837-fa812fc16a3c";

        for (CollectParam param : collectParam) {
            System.out.println(param.getCollectUrl());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", token);

            HttpEntity<String> request = null;


            String collectUrl = param.getCollectUrl();
            Map<String, HashMap<String, String>> operationParam = new HashMap<>();

            String responseDataKey = null;
            String publishedAtKey = null;

//            if (EventType.ORDER.toString().equals(String.valueOf(param.getCollectParam().get(EventType.class.getName())))) {
//                requestDeliveryEnd(operationParam);
//            }else if(EventType.RELEASE.toString().equals(String.valueOf(param.getCollectParam().get(EventType.class.getName())))){
//
//            }else if(EventType.CANCEL.toString().equals(String.valueOf(param.getCollectParam().get(EventType.class.getName())))){
//
//            }else if(EventType.RETURN.toString().equals(String.valueOf(param.getCollectParam().get(EventType.class.getName())))){
//
//            }

            switch (EventType.of(String.valueOf(param.getCollectParam().get(EventType.class.getName())))){

                case ORDER:
                    requestDeliveryEnd(operationParam);
                    responseDataKey = "deliveryEnds";
                    publishedAtKey = "paymtDt";
                case RELEASE:
                    requestWarehouseOut(operationParam);
                    responseDataKey = "deliveryEnds";
                    publishedAtKey = "paymtDt";
                case CANCEL:
                    requestOrdCancel(operationParam);
                    responseDataKey = "deliveryEnds";
                    publishedAtKey = "paymtDt";
                case EXCHANGE:
                    requestExchangeTarget(operationParam);
                    responseDataKey = "deliveryEnds";
                    publishedAtKey = "paymtDt";

            }

            Map data = null;
            try {
                request = new HttpEntity<>(objectMapper.writeValueAsString(operationParam), headers);


                String dataResult = restTemplate.postForObject(collectUrl, request , String.class);

//                FileReader dataResult = null;
//
//                try {
//                    dataResult = new FileReader("C:\\project\\argo_localtest\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\ssgDeliveryEnd");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                data = objectMapper.readValue(dataResult, Map.class);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map deliveryData = (Map) data.get("result");
            List deliveryEnds = (List) deliveryData.get(responseDataKey);
            Map deliveryEnd = (Map) deliveryEnds.get(0);
            List<HashMap<String, String>> rawEvents = (List<HashMap<String, String>>) deliveryEnd.get("deliveryEnd");

            //오더ID+오더date를 키로 사용
            // 중복 오더가 있다면  다른건으로 분류한다.
            HashMap<String, SsgRawEventParam> collatedOrder = new HashMap<>();
            rawEvents.forEach(event -> {
                String orderId = String.valueOf(event.get("orOrdNo"));
                if (collatedOrder.containsKey(orderId)) {
                    collatedOrder.get(orderId).getDataRows().add(event);
                } else {
                    SsgRawEventParam ssgRawEventParam = new SsgRawEventParam();
                    ssgRawEventParam.getDataRows().add(event);
                    //ssgRawEventParam.setPublishedAtKey(publishedAtKey);
                    collatedOrder.put(orderId, ssgRawEventParam);
                }
            });


            collatedOrder.forEach((key,event) -> {

                Map dataRows = new HashMap<>();
                dataRows.put("dataRows",event);

                RawEvent rawEvent = null;

                try {
                    rawEvent = RawEvent.builder()
                            .vendorId(vendorChannel.getVendor().getVendorId())
                            .channelId(vendorChannel.getSalesChannel().getSalesChannelId())
                            .format("JSON")
                            .auto(true)
                            .data(objectMapper.writeValueAsString(dataRows))
                            .orderId(key)
                            //.publishedAt(ArgoDateUtil.getDate(event.get(0).get(publishedAtKey).replaceAll("\\.", "-")))
                            .createdAt(new Date())
                            .build();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                rawEventService.save(rawEvent);

            });

        }
    }

    //배송완료
    public void requestDeliveryEnd(Map<String, HashMap<String, String>> operationParam){

        HashMap<String, String> map= new HashMap<>();
        operationParam.put("requestDeliveryEnd",map);
        map.put("perdType", "01");
        map.put("perdStrDts", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.put("perdEndDts", ArgoDateUtil.getDateString(LocalDate.now()));
        map.put("commType", "");
        map.put("commValue", "");

    }

    //출고지시
    public void requestWarehouseOut(Map<String, HashMap<String, String>> operationParam){

        HashMap<String, String> map= new HashMap<>();
        operationParam.put("requestWarehouseOut",map);
        map.put("perdType", "01");
        map.put("perdStrDts", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.put("perdEndDts", ArgoDateUtil.getDateString(LocalDate.now()));

    }

    //주문취소
    public void requestOrdCancel(Map<String, HashMap<String, String>> operationParam){

        HashMap<String, String> map= new HashMap<>();
        operationParam.put("requestShppDirection",map);
        map.put("perdType", "01");
        map.put("perdStrDts", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.put("perdEndDts", ArgoDateUtil.getDateString(LocalDate.now()));

    }

    //반품 교환 환불
    public void requestExchangeTarget(Map<String, HashMap<String, String>> operationParam){

        HashMap<String, String> map= new HashMap<>();
        operationParam.put("requestExchangeTarget",map);
        map.put("perdType", "01");
        map.put("perdStrDts", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.put("perdEndDts", ArgoDateUtil.getDateString(LocalDate.now()));

    }

    @Test
    public void run() throws IOException {
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==9){
                collect(channel);
            }
        }
    }
}
