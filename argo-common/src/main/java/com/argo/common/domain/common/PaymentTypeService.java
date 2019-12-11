package com.argo.common.domain.common;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.order.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConversionOperationService
public class PaymentTypeService {

    @ConversionOperationMethod
    public PaymentType getPaymentType(String paymentType) {
        String paymentTypeLowercase = paymentType.toLowerCase();
        if(paymentType.contains("카드") || paymentTypeLowercase.contains("card")) {
            return PaymentType.CREDITCARD;
        } else if(paymentType.contains("현금") || paymentTypeLowercase.contains("cash")) {
            return PaymentType.CASH;
        } else if(paymentType.contains("수표") || paymentTypeLowercase.contains("check")) {
            return PaymentType.CHECK;
        } else if(paymentType.contains("상품권") || paymentTypeLowercase.contains("giftcard")) {
            return PaymentType.GIFTCARD;
        } else if(paymentType.contains("포인트") || paymentTypeLowercase.contains("point")) {
            return PaymentType.POINT;
        } else {
            return PaymentType.ETC;
        }
    }
}
