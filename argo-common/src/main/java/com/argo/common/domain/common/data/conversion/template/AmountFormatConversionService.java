package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@ConversionOperationService
public class AmountFormatConversionService {

    @ConversionOperationMethod
    public Long convertPlayerRefundAmtFormat(String refundAmt){
            String amt = refundAmt.replaceAll(" 원", "");
            if(StringUtils.isEmpty(amt)){
                amt="0";
            }
        return Long.valueOf(amt);
    }

    @ConversionOperationMethod
    public Long replaceComa(String amount){
        String amt = amount.replaceAll(",", "");
        if(StringUtils.isEmpty(amt)){
            amt="0";
        }
        return Long.valueOf(amt);
    }
}
