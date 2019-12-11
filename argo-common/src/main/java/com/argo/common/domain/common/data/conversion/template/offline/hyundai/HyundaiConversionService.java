package com.argo.common.domain.common.data.conversion.template.offline.hyundai;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.common.util.ArgoDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@ConversionOperationService
public class HyundaiConversionService {

    @ConversionOperationMethod
    public Date getDateAndTime(String paidDate, String paidTime) {
        String paidAt = paidDate + paidTime;
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return dt.parse(paidAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
