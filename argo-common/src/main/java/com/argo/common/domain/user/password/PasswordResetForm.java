package com.argo.common.domain.user.password;

import lombok.Data;

@Data
public class PasswordResetForm {
    private String loginId;
    private String newPassword;
    private String newPasswordConfirm;
}
