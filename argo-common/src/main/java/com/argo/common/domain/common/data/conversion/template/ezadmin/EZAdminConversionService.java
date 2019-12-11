package com.argo.common.domain.common.data.conversion.template.ezadmin;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.common.util.ArgoDateUtil;
import com.argo.common.domain.order.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@ConversionOperationService
public class EZAdminConversionService {

    @ConversionOperationMethod
    public Date mergeDateAndTime(String date, String time) {
        return ArgoDateUtil.parseDateString(date + " " + time);
    }
}
