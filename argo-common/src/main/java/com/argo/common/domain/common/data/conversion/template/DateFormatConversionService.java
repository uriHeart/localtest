package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.common.util.ArgoDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Service
@ConversionOperationService
public class DateFormatConversionService {

    @ConversionOperationMethod
    public Date convertDateFormat(String date){
        return  ArgoDateUtil.getDateByTimeFormatHH(date.replaceAll("\\.", "-"));
    }


    @ConversionOperationMethod
    public Date convertDateFormatUTC(String date){
        Date kst = ArgoDateUtil.getDateByTimeFormatHH(date.replaceAll("\\.", "-"));

        /**
         * ordermeta 저장시 kst로 저장된후 조회시 utc로 인식하여 +9시간되는것을 보정하기위해
         * utc로 저장함.
         * TODO 조회시 kst로 저장되었음을 인식하도록 변경
         * ezadmin 데이터에 영향이 있을수 있음으로 수집시에 utc로 수정함.
         */
        LocalDateTime utc = LocalDateTime.ofInstant(kst.toInstant(), ZoneId.of("UTC"));
        Date utcDate = ArgoDateUtil.getDateByTimeFormatHH(utc.minusHours(9L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return utcDate;
    }

    @ConversionOperationMethod
    public Date addTime(String date){
        String dateTime = date+" 00:00:00";
        return ArgoDateUtil.getDate(dateTime);
    }

}