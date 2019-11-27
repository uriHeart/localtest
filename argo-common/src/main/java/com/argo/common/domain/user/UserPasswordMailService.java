package com.argo.common.domain.user;

import com.argo.common.domain.common.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordMailService {
    @Value("${passwordreset.url.confirm}")
    private String passwordResetConfirmUrl;

    @Autowired
    private EmailService emailService;

    public void sendPasswordResetConfirm(ArgoUser user, String token) {
        String subject = "[ARGO] 계정 비밀번호 리셋을 신청하셨습니다.";
        String content = "안녕하세요, {name} 님.\n"
                             + "ARGO 계정의 비밀번호를 리셋하기 위해 아래 링크를 클릭하시고 진행하여 주시기 바랍니다.\n"
                             + "{passwordResetLink}\n"
                             + "\n"
                             + "감사합니다.\n"
                             + "ARGO 드림";
        emailService.sendSimpleMessage(
            user.getLoginId(),
            subject,
            content.replace("{name}", user.getUserName())
                .replace("{passwordResetLink}", passwordResetConfirmUrl + "/" + token)
        );
    }
}
