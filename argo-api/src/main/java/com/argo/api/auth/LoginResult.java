package com.argo.api.auth;

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
    private String message;
    private Long vendorId;
    private String dashboardUrl;
    private String totalDashboardUrl;
}
