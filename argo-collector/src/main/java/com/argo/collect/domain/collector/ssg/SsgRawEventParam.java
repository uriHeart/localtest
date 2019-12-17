package com.argo.collect.domain.collector.ssg;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SsgRawEventParam {

    private List<HashMap<String,String>> dataRows = new ArrayList<>();

    private String publishedAtKey;

    private String responseDataKey;

    private String orderId;

}
