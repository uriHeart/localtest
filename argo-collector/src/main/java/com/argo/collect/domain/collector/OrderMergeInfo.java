package com.argo.collect.domain.collector;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class OrderMergeInfo {
    String orderIdFieldKey;
    String publishedAtFieldKey;
    String eventTypeFieldKey;
}
