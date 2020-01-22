package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@ConversionOperationService
public class AmountCalculateService {

    @ConversionOperationMethod
    public Long getSumPrice(String price, String qty){
            long amt = Long.valueOf(price) * Long.valueOf(qty);

        return amt;
    }
}
