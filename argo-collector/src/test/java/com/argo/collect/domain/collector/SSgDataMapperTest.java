package com.argo.collect.domain.collector;

import com.argo.common.domain.common.jpa.EventType;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSgDataMapperTest {


    public static void main(String[] args) throws JSONException, JsonProcessingException {

        System.out.println(EventType.class.getSimpleName());

        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();


        String dataUrl = "http://eapi.ssgadm.com/api/pd/1/listDeliveryEnd.ssg";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "03e5cca2-08dc-4a37-9837-fa812fc16a3c");

        Map<String, HashMap<String, String>> operation= new HashMap<>();

        ObjectMapper parmaMapper = new ObjectMapper();

        HashMap<String, String> map= new HashMap<>();
        operation.put("requestDeliveryEnd",map);
        map.put("perdType", "01");
        map.put("perdStrDts", ArgoDateUtil.getDateString(LocalDate.now().minusMonths(1)));
        map.put("perdEndDts", ArgoDateUtil.getDateString(LocalDate.now()));
        map.put("commType", "");
        map.put("commValue", "");
        HttpEntity<String> request = new HttpEntity<>(parmaMapper.writeValueAsString(operation), headers);
        //String dataResults = restTemplate.postForObject(dataUrl, request , String.class);


        FileReader dataResult = null;
        try {
            dataResult = new FileReader("C:\\project\\localtest\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\ssgDeliveryEnd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Map data = objectMapper.readValue(dataResult, Map.class);
            Map deliveryData = (Map) data.get("result");
            List deliveryEnds = (List) deliveryData.get("deliveryEnds");
            Map  deliveryEnd = (Map) deliveryEnds.get(0);
            List<HashMap> rawEvents = (List<HashMap>) deliveryEnd.get("deliveryEnd");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
