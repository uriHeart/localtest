package com.argo.collect.domain.collector;


import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.collector.claim.MusinsaClaimHandler;
import com.argo.collect.domain.collector.claim.PlayerClaimHandler;
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
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(
//        properties = {
//                "value=test"
//        },
//        classes = {ArgoCollectorApplication.class},
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//)
public class PlayerCollectorTest extends AbstractOrderCollector {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private List<EventConverter> eventConverter;

    @Autowired
    private List<PlayerClaimHandler> claimHandlers;

    @Override
    public boolean isSupport(SalesChannel channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel channel) {

        AuthorityManager authorityManager = super.getAuth(channel.getSalesChannel().getCode());
        String authorization = authorityManager.requestAuth(channel);
        CollectParam collectParam = super.getCollectInfo(channel);

        String dataUrl = collectParam.getCollectUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", authorization);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("S_SDATE", "2020-01-01 00:00:00");

        map.add("S_EDATE", ArgoDateUtil.getDateString(LocalDate.now()));

        //https://biz.player.co.kr/po/order/ord01/detail?ORD_NO=20191017195084f2fe&ORD_OPT_NO=13931018&LAYOUT_TYPE=popup

        //수집대상 호출부분
        List<Map> eventList =null;
        try {
            eventList = (List<Map>) this.getUrlCallResultByPost(dataUrl,headers,map).get("data");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map> claimList =new ArrayList<>();

        eventList.forEach(event -> {

            String detailUrl = "https://biz.player.co.kr/po/order/ord01/detail";
            HttpHeaders detailHeaders = new HttpHeaders();
            detailHeaders.add("cookie", authorization);


            String orderDetail= null;
            try {
                orderDetail = this.getUrlCallResultByGet(detailUrl,authorization,event);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Document doc =Jsoup.parse(orderDetail);

            //0:주문자 정보
            //1:수령자 정보
            //2:상품 정보
            //3:클레임상태
            //4:클레임내용 <tbody>
            //5:클레임구분
            //8:처리일자 정보
            List<Element> tbodyList = doc.body().getElementsByTag("tbody");

            event.putAll(this.getDetailData(tbodyList,1));

            event.putAll(this.getDetailData(tbodyList,3));

            //to_do outbound error
            //event.put("clm_message",tbodyList.get(4).childNodes().get(1).childNode(0).childNode(0));

            event.putAll(this.getDetailData(tbodyList,8));

            List<Map> claim = claimHandlers
                    .stream()
                    .filter(s ->s.isClaim(event))
                    .map(s -> s.makeClaim(event))
                    .collect(Collectors.toList())
                    ;

            claimList.addAll(claim
                    .stream()
                    .filter(s->s!=null && !s.isEmpty())
                    .collect(Collectors.toList())
            );

        });

        eventList.addAll(claimList);

        this.eventConvert(eventList,channel);

        HashMap<String, RawEventParam> mergedOrder = this.mergeRawEvent(eventList);

        this.saveRawData(mergedOrder, channel);
    }

    public Map getDetailData(List<Element> tbodyList,int detailTarget){
        Map detailData = new HashMap();

        List<Object> data =
                tbodyList.get(detailTarget)
                        .childNodes()
                        .stream()
                        .map(tr-> tr.childNodes()
                                .stream()
                                .filter(td -> !" ".equals(td.toString()))
                                .filter(td -> !StringUtils.isEmpty(td.toString()))
                                .map(td-> {
                                    String result;
                                    if(td.childNodes().size()==0){
                                        result ="";
                                    }else if(td.childNodes().get(0).childNodes().size()==0){
                                        //ex) data
                                        //<a href="#" onclick="openWindow('/po/order/ord01/delivery?ORD_NO=20191017195084f2fe&ORD_OPT_NO=13931018','order_dlv','resizable=yes,scrollbars=yes', '779', '569');return false;" title="20191017195084f2fe 배송 정보 수정">전상준</a>
                                        Node node = td.childNodes()
                                                .stream()
                                                .filter(s -> !" ".equals(s.toString()))
                                                .filter(s -> !StringUtils.isEmpty(s.toString()))
                                                .findFirst()
                                                .get()
                                                ;
                                        return node.childNodes().size()==0?node.toString():node.childNodes().get(0).toString();
                                    }else {
                                        result = td.childNodes().get(0).childNodes().get(0).toString();
                                    }
                                    return result;
                                })
                        )
                        .flatMap(value -> value)
                        .collect(Collectors.toList())
                        ;
        for(int i=2 ;i <= data.size(); i+=2){
            detailData.put(data.get(i-2),data.get(i-1));
        }
        return detailData;
    }


    public List<Map> eventConvert(List<Map> eventList, VendorChannel channel){

        eventList.forEach(event ->{
            //이벤트 타입변환
            EventType eventType = eventConverter.stream()
                    .filter(converter -> converter.isSupport(channel.getSalesChannel().getCode()))
                    .map(converter -> converter.getEventType(event))
                    .findFirst()
                    .orElse(EventType.OTHER);

            event.put("event_type",eventType.toString());
        });

        return eventList;
    }


    public Map getUrlCallResultByPost(String dataUrl, HttpHeaders headers, MultiValueMap<String, String> paramMap) throws IOException {

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, headers);
        String dataResult = restTemplate.postForObject(dataUrl, request , String.class);
        Map data = objectMapper.readValue(dataResult, Map.class);

        return data;
    }

    public String getUrlCallResultByGet(String dataUrl, String authorization,Map event) throws IOException {

        HttpResponse<String> response = Unirest.get("https://biz.player.co.kr/po/order/ord01/detail")
                                .header("Cookie",authorization)
                                .queryString("ORD_NO",String.valueOf(event.get("ord_no")))
                                .queryString("ORD_OPT_NO",String.valueOf(event.get("ord_opt_no")))
                                .queryString("LAYOUT_TYPE","popup")
                                .asString();

        return response.getBody();
    }


    public void saveRawData(HashMap<String, RawEventParam> mergedOrder, VendorChannel channel){

        mergedOrder.forEach((key,event)->{
            String eventToJson = null;

            try {
                eventToJson = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            RawEvent rawEvent = RawEvent.builder()
                    .vendorId(channel.getVendor().getVendorId())
                    .channelId(channel.getSalesChannel().getSalesChannelId())
                    .format("JSON")
                    .auto(true)
                    .data(eventToJson)
                    .orderId(key)
                    .publishedAt(ArgoDateUtil.getDate(event.getPublishedAt().replaceAll("\\.", "-")))
                    .createdAt(new Date())
                    .event(event.getEventType())
                    .build();
            rawEventService.save(rawEvent);
        });
    }

    public HashMap<String, RawEventParam> mergeRawEvent(List<Map> orderList){

        HashMap<String, RawEventParam> mergedOrder = new HashMap<>();
        orderList.forEach(event -> {
            String orderId = String.valueOf(event.get("ord_no"));
            String publishedAt = String.valueOf(event.get("upd_date"));
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

    @Test
    public void run(){
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==1){
                collect(channel);
            }
        }
    }
}
