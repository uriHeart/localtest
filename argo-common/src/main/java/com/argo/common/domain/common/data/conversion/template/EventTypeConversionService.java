package com.argo.common.domain.common.data.conversion.template;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.common.jpa.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConversionOperationService
public class EventTypeConversionService {

    @ConversionOperationMethod
    public String findEventTypeName(String event_type){
        EventType e = EventType.of(event_type);
        return e.name();
    }

}
