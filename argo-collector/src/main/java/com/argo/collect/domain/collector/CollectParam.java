package com.argo.collect.domain.collector;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Setter
@Getter
public class CollectParam {
    private String collectUrl;
    private String collectDetailUrl;
    private Map collectParam;
}
