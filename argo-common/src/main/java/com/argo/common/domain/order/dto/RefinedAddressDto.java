package com.argo.common.domain.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefinedAddressDto {
    private String roadAddress; //정제된 도로명 주소
    private String jibunAddress; //정제된 지번 주소
    private String roadName; // 정제된 도로명
    private String postalCode5; // 정제된 5자리 우편번호
    private String postalCode6; // 정제된 6자리 우편번호
    private Long buildingMainNumber; // 정제된 건물 주번지
    private Long buildingSubNumber; // 정제된 건물 부번지
    private String buildingName; // 정제된 건물 이름
    private String buildingDong; // 정제된 건물 동
    private String buildingHo; // 정제된 건물 호
    private Double longitude; // 경도
    private Double latitude; // 위도
}
