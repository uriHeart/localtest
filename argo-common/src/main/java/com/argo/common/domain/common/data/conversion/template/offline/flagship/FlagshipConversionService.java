package com.argo.common.domain.common.data.conversion.template.offline.flagship;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.order.PaymentType;
import org.springframework.stereotype.Service;

@Service
@ConversionOperationService
public class FlagshipConversionService {

    @ConversionOperationMethod
    public PaymentType getPaymentType(Long creditcardAmount, Long cashAmount, Long checkAmount, Long giftCardAmount, Long creditAmount, Long pointAmount) {
        if(creditcardAmount > 0) {
            return PaymentType.CREDITCARD;
        } else if(cashAmount > 0) {
            return PaymentType.CASH;
        } else if (checkAmount > 0) {
            return PaymentType.CHECK;
        } else if (giftCardAmount > 0) {
            return PaymentType.GIFTCARD;
        } else if (pointAmount > 0) {
            return PaymentType.POINT;
        } else {
            return PaymentType.ETC;
        }
    }
}
