package com.argo.common.domain.user;

import com.argo.common.domain.common.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegistrationConfirmMailService {

    @Value("${registration.url.confirm}")
    private String confirmLink;

    @Value("${registration.url.additional-info")
    private String additionalInfoLink;

    @Autowired
    private EmailService emailService;

    void sendConfirmationMail(ArgoUser user, String uuidForConfirm) {
        String subject = "[ARGO] 계정을 등록하셨습니다.";
        String content = "안녕하세요 {name} 님.\n"
                             + "  ARGO 계정에 등록하신 이메일 주소를 인증하기 위해 아래 링크를 클릭하여 주시기 바랍니다.\n"
                             + "  {confirmLink}\n"
                             + "\n"
                             + "  감사합니다.\n"
                             + "  ARGO 드림";

        emailService.sendSimpleMessage(user.getLoginId(),
            subject,
            content.replace("{name}", user.getUserName())
                .replace("{confirmLink}", confirmLink + "/" + uuidForConfirm));
    }

    void sendCompletionMail(ArgoUser user) {
        String subject = "[ARGO] 이메일 주소 인증이 완료되었습니다.";
        String content = "안녕하세요 {name} 님.\n"
                             + "  고객님의 {mail} 메일 주소는 정상적인 주소로 확인되었습니다.\n"
                             + "  아래 링크를 클릭하거나 등록하신 이메일 주소와 비밀번호로 로그인하셔서 나머지 정보를 등록하여 주시기 바랍니다.\n"
                             + "  {additionalInfo}\n"
                             + "  감사합니다.\n"
                             + "  ARGO 드림";

        emailService.sendSimpleMessage(user.getLoginId(),
            subject,
            content.replace("{name}", user.getUserName())
                .replace("{mail}", user.getLoginId()).replace("{additionalInfoLink}", additionalInfoLink));
    }
}
