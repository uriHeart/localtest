package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.collector.claim.MusinsaClaimHandler;
import com.argo.collect.domain.event.EventConverter;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class MusinsaOrderCollector extends AbstractOrderCollector {


    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final List<EventConverter> eventConverter;

    private final List<MusinsaClaimHandler> claimHandlers;

    @Override
    public boolean isSupport(SalesChannel channel) {
        return "MUSINSA".equals(channel.getCode());
    }

    @Override
    public void collect(VendorChannel channel) {

        AuthorityManager authorityManager = super.getAuth(channel.getSalesChannel().getCode());
        String authorization = authorityManager.requestAuth(channel);
        CollectParam collectParam = super.getCollectInfo(channel);

        String dataUrl = collectParam.getCollectUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("cookie", authorization);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("S_SDATE", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.add("S_EDATE", ArgoDateUtil.getDateString(LocalDate.now()));
        map.add("MENU_ID", "/po/order/ord01");
        map.add("LIMIT", "1000");



        //수집대상 호출부분
        List<Map> eventList =null;
        try {
            eventList = (List<Map>) this.getUrlCallResult(dataUrl,headers,map).get("data");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map> claimList =new ArrayList<>();

        eventList.forEach(event -> {


            //상세데이터조회
            String detailUrl = "https://bizest.musinsa.com/po/api/order/ord01/get_detail";
            String ORD_NO = event.get("ord_no").toString();
            String ORD_OPT_NO = event.get("ord_opt_no").toString();

            //마스킹해제 파라미터
            String p_user_ord_cnt = "p_user_ord_cnt=goal%7C6";
            String p_user_ord_no = "p_user_ord_no=goal%7C"+ORD_NO;

            String cookieDetail = authorization+"; "+p_user_ord_cnt+"; "+p_user_ord_no;

            MultiValueMap<String, String> detailMap= new LinkedMultiValueMap<>();
            detailMap.add("ORD_NO", ORD_NO);
            detailMap.add("ORD_OPT_NO", ORD_OPT_NO);

            HttpHeaders detailHeaders = new HttpHeaders();
            detailHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            detailHeaders.add("cookie", cookieDetail);

            Map orderDetail= null;
            try {
                orderDetail = this.getUrlCallResult(detailUrl,detailHeaders,detailMap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map detailData = (Map) orderDetail.get("order");
            event.putAll(detailData);

            //오더별로 묶여진 pay
            Map payment = (Map) orderDetail.get("payment");
            event.putAll(payment);

            //claim
            Map claimData = (Map) orderDetail.get("claim");
            //불필요 기준데이터 삭제
            claimData.remove("BANK_CODES");
            claimData.remove("CLM_REASONS");

            event.putAll(claimData);

            Map claim = claimHandlers
                    .stream()
                    .filter(s ->s.isClaim(event))
                    .map(s -> s.makeClaim(event))
                    .findFirst()
                    .orElse(null)
                    ;
            if(claim!=null && !claim.isEmpty()) claimList.add(claim);

        });

        eventList.addAll(claimList);

        this.eventConvert(eventList,channel);

        HashMap<String, RawEventParam> mergedOrder = this.mergeRawEvent(eventList);

        this.saveRawData(mergedOrder, channel);

    }

    public List<Map> eventConvert(List<Map> eventList,VendorChannel channel){

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

    public Map getUrlCallResult(String dataUrl, HttpHeaders headers, MultiValueMap<String, String> paramMap) throws IOException {

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, headers);
        String dataResult = restTemplate.postForObject(dataUrl, request , String.class);

        Map data = objectMapper.readValue(dataResult, Map.class);

        return data;
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
            if (mergedOrder.containsKey(orderId)) {
                mergedOrder.get(orderId).getDataRows().add(event);
            } else {
                RawEventParam rawEventParam = new RawEventParam();
                rawEventParam.setOrderId(orderId);
                rawEventParam.setPublishedAt(publishedAt);
                rawEventParam.setEventType(eventType);
                rawEventParam.getDataRows().add(event);
                mergedOrder.put(orderId, rawEventParam);
            }
        });

        return mergedOrder;
    }
}
