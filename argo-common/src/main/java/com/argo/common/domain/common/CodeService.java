package com.argo.common.domain.common;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@ConversionOperationService
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

    @ConversionOperationMethod
    public String getOrderStatusMapping(String code) {
        return codeGroups.get("orderStatus").getOrDefault(code, null);
    }
}
