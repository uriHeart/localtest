package com.argo.collect.domain.collector;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.auth.AuthorityManager;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class MusinsaOrderCollectorTest  extends AbstractOrderCollector {

    @Autowired
    private VendorService vendorService;

    @Override
    public boolean isSupport(String channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel channel) {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

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
//        map.add("USR_SEARCH_ITEM_CNT", "1");
//        map.add("S_COLS", "b.mobile");
//
//        map.add("MENU_ID", "/po/order/ord01");
//        map.add("USR_SEARCH_ITEM_CNT", "1");
//        map.add("S_COLS", "b.mobile");
//        map.add("PAGE_CNT", "10");
//        map.add("PAGE", "1");
//        map.add("SORT", "desc");
//        map.add("ORD_FIELD", "a.ord_date");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String dataResult = restTemplate.postForObject(dataUrl, request , String.class);
        //https://bizest.musinsa.com/po/order/ord01/detail?ORD_NO=201912021211325925&ORD_OPT_NO=45145228&LAYOUT_TYPE=popup

        try {
            Map data = objectMapper.readValue(dataResult, Map.class);
            List<Map> rawEvents = (List<Map>) data.get("data");
            rawEvents.forEach(
                    event -> {

                        String detailUrl ="https://bizest.musinsa.com/po/api/order/ord01/get_detail";
                        String ORD_NO = event.get("ord_no").toString();
                        String ORD_OPT_NO = event.get("ord_opt_no").toString();



                        String urlParameters ="ORD_NO="+ORD_NO;
                        HttpURLConnection con =null;
                        try {
                            URL obj = new URL("https://bizest.musinsa.com/po/api/order/ord01/inquiry_privacy");
                            con = (HttpURLConnection) obj.openConnection();

                            con.setRequestMethod(HttpMethod.POST.name());

                            con.setRequestProperty("cookie", authorization);
                            con.setDoOutput(true);
                            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                            wr.writeBytes(urlParameters);
                            wr.flush();
                            wr.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        List<String> cookie = con.getHeaderFields().get("Set-Cookie");
//                        String p_user_ord_cnt = cookie.get(0).split(";")[0];
//                        String p_user_ord_no = cookie.get(1).split(";")[0];
                        String p_user_ord_cnt = "p_user_ord_cnt=goal%7C6";
                        String p_user_ord_no = "p_user_ord_no=goal%7C"+ORD_NO;
                        String auth2 = authorization+"; "+p_user_ord_cnt+"; "+p_user_ord_no;

                        MultiValueMap<String, String> detailMap2= new LinkedMultiValueMap<>();
                        detailMap2.add("ORD_NO", ORD_NO);
                        detailMap2.add("ORD_OPT_NO", ORD_OPT_NO);

                        HttpHeaders headers2 = new HttpHeaders();
                        headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                        headers2.add("cookie", auth2);
                        HttpEntity<MultiValueMap<String, String>> detailRequest2 = new HttpEntity<>(detailMap2, headers2);
                        String detailResult2 = restTemplate.postForObject(detailUrl, detailRequest2 , String.class);



                        try {
                            RawEvent rawEvent = RawEvent.builder()
                                    .vendorId(channel.getVendor().getVendorId())
                                    .channelId(channel.getSalesChannel().getSalesChannelId())
                                    .format("JSON")
                                    .auto(true)
                                    .data(objectMapper.writeValueAsString(event))
                                    .orderId(event.get("ord_no").toString())
                                    .publishedAt(ArgoDateUtil.getDate(event.get("ord_date").toString().replaceAll("\\.", "-")))
                                    .createdAt(new Date())
                                    .build();
                            rawEventService.save(rawEvent);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void run(){
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==8){
                collect(channel);
            }
        }
    }
}
