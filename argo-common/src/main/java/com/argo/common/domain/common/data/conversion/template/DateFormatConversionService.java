package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.common.util.ArgoDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@ConversionOperationService
public class DateFormatConversionService {

    @ConversionOperationMethod
    public Date convertDateFormat(String date){
        return ArgoDateUtil.getDate(date.replaceAll("\\.", "-"));
    }

    @ConversionOperationMethod
    public Date addTime(String date){
        String dateTime = date+" 00:00:00";
        return ArgoDateUtil.getDate(dateTime);
    }

}
