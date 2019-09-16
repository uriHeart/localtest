package com.argo.api.controller.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipientDto {
    private String name; //수취인 이름
    private String phoneNumber1; //수취인 전화번호1
    private String phoneNumber2; //수취인 전화번호2
}
