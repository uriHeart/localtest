package com.argo.common.domain.common;

import org.springframework.stereotype.Service;
import org.testng.collections.Maps;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class CodeService {
    private Map<String, Map<String, String>> codeGroups;

    @PostConstruct
    public void init() {
        codeGroups = Maps.newHashMap();
        Map<String, String> status = Maps.newHashMap();
        status.put("0", "발주");
        status.put("1", "접수");
        status.put("7", "송장출력");
        status.put("8", "배송");
        codeGroups.put("orderStatus", status);
    }

    public String getOrderStatusMapping(String code) {
        return codeGroups.get("orderStatus").getOrDefault(code, null);
    }
}
