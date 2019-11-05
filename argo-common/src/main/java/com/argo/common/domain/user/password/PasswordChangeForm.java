package com.argo.common.domain.user.password;

import lombok.Data;

@Data
public class PasswordChangeForm {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}