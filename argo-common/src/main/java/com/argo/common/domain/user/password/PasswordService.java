package com.argo.common.domain.user.password;

import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserPasswordMailService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PasswordService {

    private PasswordRecoveryRepository passwordRecoveryRepository;

    private RsaDecrypter rsaDecrypter;

    private HttpSession httpSession;

    private UserPasswordMailService userPasswordMailService;

    public static final long LONG_RECOVER_TOKEN_TIMEOUT_MS = 60 * 60 * 24 * 1000;
    public static final int MAX_RECOVERY_ATTEMPTS = 500;

    @Transactional
    public List<PasswordRecovery> deactivateTokenForUser(ArgoUser user) {
        List<PasswordRecovery> passwordRecoveryList = passwordRecoveryRepository.findAllByArgoUser(user);
        passwordRecoveryList.forEach((passwordRecovery) -> {
            passwordRecovery.setActive(false);
        });
        PasswordRecovery newPasswordRecovery = PasswordRecovery.builder()
            .active(true)
            .argoUser(user)
            .token(UUID.randomUUID().toString())
            .build();
        passwordRecoveryList.add(newPasswordRecovery);
        userPasswordMailService.sendPasswordResetConfirm(user, newPasswordRecovery.getToken());
        return passwordRecoveryRepository.saveAll(passwordRecoveryList);
    }

    public void deleteTokenForUser(ArgoUser user) {
        passwordRecoveryRepository.deleteAllByArgoUser(user);
    }

    public PasswordRecovery getActivePasswordRecoveryByToken(String token) {
        return passwordRecoveryRepository.findByTokenAndActive(token, true);
    }

    public Long getPasswordRecoveryCountByUser(ArgoUser user) {
        return passwordRecoveryRepository.countByArgoUser(user);
    }

    public String decryptPassword(String password) {
        String rsaPrivateKey = httpSession.getAttribute("_RSA_WEB_Key_").toString();
        return rsaDecrypter.decryptRsa(password, rsaPrivateKey);
    }

    public boolean isTokenValid(PasswordRecovery passwordRecovery) {
        return passwordRecovery.getActive() && ((new Date()).getTime() - passwordRecovery.getCreatedAt().getTime()) < LONG_RECOVER_TOKEN_TIMEOUT_MS;
    }

    public PasswordRecovery deactivatePasswordRecovery(PasswordRecovery passwordRecovery) {
        passwordRecovery.setActive(false);
        return passwordRecoveryRepository.save(passwordRecovery);
    }
}
