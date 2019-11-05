package com.argo.common.domain.user;

import com.argo.common.domain.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpMailService {

    private String subject = "[ARGO] 계정을 등록하셨습니다.";

    private String content = "안녕하세요 {name} 님.\n"
                                 + "  ARGO 계정에 등록하신 이메일 주소를 인증하기 위해 아래 링크를 클릭하여 주시기 바랍니다.\n"
                                 + "  {confirmLink}\n"
                                 + "\n"
                                 + "  감사합니다.\n"
                                 + "  ARGO 드림";

    @Autowired
    private AuthService authService;

    public void sendConfirmMail(Seller user) {
        String uuidForConfirm = authService.createUuidForConfirm(user);
        log.info(uuidForConfirm);
    }

}
