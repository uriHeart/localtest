package com.argo.collect.domain.collector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RawEventParam {

    private List<Map<String,String>> dataRows = new ArrayList<>();

    private String publishedAt;

    private String orderId;
}
