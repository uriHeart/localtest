package com.argo.common.domain.address.vworld;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefinedStructure {
    private String level0;
    private String level1;
    private String level2;
    private String level3;
    private String level4L;
    private String level4LC;
    private String level4A;
    private String level4AC;
    private String level5;
    private String detail;
}
