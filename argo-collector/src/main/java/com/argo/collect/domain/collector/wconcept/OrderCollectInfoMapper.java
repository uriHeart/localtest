package com.argo.collect.domain.collector.wconcept;

import com.argo.collect.domain.collector.CollectParam;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface OrderCollectInfoMapper {

    default Map<String, CollectParam> getCollectingInfo(List<CollectParam> collectParam){
        return collectParam
                .stream()
                .filter(param ->!StringUtils.isEmpty(param.getCollectParam().get("collectType")))
                .collect(Collectors.toMap(param-> String.valueOf(param.getCollectParam().get("collectType")), param->param))
                ;
    }

    Map<String,String> makeRequestParam(CollectParam collectParam);
}
