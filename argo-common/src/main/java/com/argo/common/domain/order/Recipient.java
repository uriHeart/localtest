package com.argo.common.domain.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipient {
    private String name; //수취인 이름
    private String phoneNumber1; //수취인 전화번호1
    private String phoneNumber2; //수취인 전화번호2
}
