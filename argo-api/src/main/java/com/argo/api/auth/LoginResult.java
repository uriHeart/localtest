package com.argo.api.auth;

import com.argo.common.domain.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    private boolean success;
    private String userId;
    private String message;
    private UserStatus userStatus;
    private Long vendorId;
    private String dashboardUrl;
    private String totalDashboardUrl;
}

