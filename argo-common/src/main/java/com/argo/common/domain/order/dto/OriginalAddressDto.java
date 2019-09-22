package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OriginalAddressDto {
    private String address1; //고객 원본 주소1
    private String address2; //고객 원본 주소2
    private String fullAddress; //고객 원본 전체주소
    private String postalCode; //고객 원본 우편번호
}
