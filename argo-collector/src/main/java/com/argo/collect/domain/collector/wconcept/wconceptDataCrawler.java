package com.argo.collect.domain.collector.wconcept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface wconceptDataCrawler {

    default List<Map> basicDataCrawling(){
        List<Map> result = new ArrayList<>();
        return result;
    }
}
