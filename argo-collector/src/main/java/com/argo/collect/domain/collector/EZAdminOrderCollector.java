package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.enums.SalesChannel;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class EZAdminOrderCollector extends AbstractOrderCollector {

    private static final int ROW_SIZE = 50;
    private static final int MAX_ROW = 500;

    @Override
    public boolean isSupport(String channel) {
        return "EZ_ADMIN".equals(channel);
    }

    @Override
    public void collect(VendorChannel channel) {
        AuthorityManager authorityManager = super.getAuth(channel.getSalesChannel().getCode());
        String authorization = authorityManager.requestAuth(channel);
        log.info("authorization - {}", authorization);

        CollectParam collectParam = super.getCollectInfo(channel);

        String dataUrl = collectParam.getCollectUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", authorization);

        String params = "date_type=collect_date" +
                "&start_date=" + ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)) +
                "&end_date=" + ArgoDateUtil.getDateString(LocalDate.now()) +
                "&date_period_sel=0&search_type=0&shop_id=&order_status=-1&order_cs=0&query_trans_who=0&is_gift=0&work_type=0";
        int total = this.getTotal(dataUrl, headers, params);
        int page = 1;
        while (page<= total) {
            this.saveOrder(channel, dataUrl, headers, page, params);
            page++;
        }
    }

    private int getTotal(String url, HttpHeaders headers, String parameters) {
        Map result = this.getData(url, headers, 1, ROW_SIZE, "query_json", parameters, null);
        return Integer.parseInt(result.get("total").toString());
    }

    private void saveOrder(VendorChannel channel, String url, HttpHeaders headers, int page, String parameters) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = this.getData(url, headers, page, ROW_SIZE, "query_json", parameters, null);
        List<Map> orders = (List<Map>) result.get("rows");
        orders.forEach(
                o -> {
                    Map order = (Map) o.get("cell");
                    this.setItem(url, headers, order);
                    try {
                        log.info("order - {}", objectMapper.writeValueAsString(order));
                        List<Map> dataRows = (List<Map>) order.get("dataRows");
                        Map dataRow = dataRows.get(0);
                        String collectDate = dataRow.get("collect_date").toString() + " " + dataRow.get("collect_time").toString();
                        String orderDate = dataRow.get("order_date").toString() + " " + dataRow.get("order_time").toString();

                        RawEvent rawEvent = RawEvent.builder()
                                .vendorId(channel.getVendor().getVendorId())
                                .channelId(channel.getSalesChannel().getSalesChannelId())
                                .format("JSON")
                                .auto(true)
                                .data(objectMapper.writeValueAsString(order))
                                .orderId(order.get("order_id").toString())
                                .rawEventId(UUID.randomUUID().toString())
                                .publishedAt(ArgoDateUtil.getDate(orderDate.startsWith("0000-00-00") ? collectDate : orderDate))
                                .createdAt(new Date())
                                .build();
                        rawEventService.save(rawEvent);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
        log.info("order - {}", result);
    }

    private void setItem(String url, HttpHeaders headers, Map order) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = this.getData(url, headers, 0, MAX_ROW, "packlist_json", null, order.get("pack").toString());
        List<Map> items = (List<Map>) result.get("rows");
        List<Map> vendorItems = Lists.newArrayList();
        List<Map> dataRows = Lists.newArrayList();
        items.forEach(
                i -> {
                    Map item = (Map) i.get("cell");
                    vendorItems.add(item);
//                    order.put("vendorItem", item);
                    try {
//                        order.put("dataRow", objectMapper.readValue(item.get("data_row").toString(), Map.class));
                        dataRows.add(objectMapper.readValue(item.get("data_row").toString(), Map.class));
                        item.remove("data_row");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        order.put("vendorItems", vendorItems);
        order.put("dataRows", dataRows);
    }

    private Map getData(String url, HttpHeaders headers, int page, int rows,
                        String action, String parameters, String pack) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("rows", String.valueOf(rows == 0 ? ROW_SIZE : rows));
        if (page != 0) {
            map.add("page", String.valueOf(page));
        }
        map.add("template", "E900");
        map.add("action", action);
        if (parameters != null) {
            map.add("par", parameters);
        }
        if (pack != null) {
            map.add("pack", pack);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String result = restTemplate.postForObject(url, request , String.class);
        try {
            return objectMapper.readValue(result, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
