package com.argo.common.domain.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipient {
    private String name; //수취인 이름 (dataRows.recv_name)
    private String phoneNumber1; //수취인 전화번호1 (dataRows.recv_tel1)
    private String phoneNumber2; //수취인 전화번호2 (dataRows.recv_tel2)
}
