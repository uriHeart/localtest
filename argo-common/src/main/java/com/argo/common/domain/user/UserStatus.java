package com.argo.common.domain.user;

public enum UserStatus {
    INITIALIZED, // 사용자 등록
    AUTHENTICATED, // 메일인증
    ACTIVATED      // 추가정보 입력
}
