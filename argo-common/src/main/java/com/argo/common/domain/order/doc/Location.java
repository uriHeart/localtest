package com.argo.common.domain.order.doc;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {
    private Double lat;
    private Double lon;
}
