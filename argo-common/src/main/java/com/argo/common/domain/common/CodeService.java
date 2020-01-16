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
        status.put("ORDER", "주문완료");
        status.put("CANCEL", "주문취소");
        status.put("PAYMENT_COMPLETE", "결제완료");
        status.put("RELEASE_REQUEST", "출고요청");
        status.put("RELEASE", "출고완료");
        status.put("DELIVERY", "배송중");
        status.put("DELIVERY_COMPLETE", "배송완료");
        status.put("EXCHANGE_REQUEST", "교환요청");
        status.put("EXCHANGE_PROCESS", "교환처리");
        status.put("EXCHANGE", "교환완료");
        status.put("RETURN_REQUEST", "환불요청");
        status.put("RETURN_PROCESS", "환불처리");
        status.put("RETURN", "환불완료");
        status.put("CLAIM_CANCEL", "클레임무효");
        status.put("OTHER", "기타");
        codeGroups.put("orderStatus", status);
    }

    @ConversionOperationMethod
    public String getOrderStatusMapping(String code) {
        return codeGroups.get("orderStatus").getOrDefault(code, null);
    }
}
