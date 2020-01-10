package com.argo.common.domain.user;

import com.argo.common.domain.auth.AuthService;
import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.domain.common.util.HashUtil;
import com.argo.common.domain.user.password.PasswordService;
import com.argo.common.exception.UserRegistrationException;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private RsaDecrypter rsaDecrypter;
    private PasswordService passwordService;
    private AuthService authService;
    private UserRegistrationConfirmMailService userRegistrationConfirmMailService;

    public ArgoUser getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Transactional
    public void addSeller(AddUserForm addUserForm, String rsaPrivateKey) {
        assertValid(addUserForm);
        ArgoUser newSeller = userRepository.save(addUserForm.toSellerEntity(rsaDecrypter, rsaPrivateKey));
        String uuidForConfirm = authService.createUuidForConfirm(newSeller);
        userRegistrationConfirmMailService.sendConfirmationMail(newSeller, uuidForConfirm);
    }

    private void assertValid(AddUserForm form) {
        if (userRepository.existsByLoginId(form.getEmail())) {
            throw new UserRegistrationException("이미 등록된 메일주소 입니다.");
        }
    }

    public ArgoUser updateUser(ArgoUser inputUser, String email) {
        ArgoUser user = userRepository.findByLoginId(email);
        // update user with not null values
        if (inputUser.getLoginId() != null) {
            user.setLoginId(inputUser.getLoginId());
        }

        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    @Transactional
    public ArgoUser resetPassword(String email, String password) {
        ArgoUser user = getUserByLoginId(email);
        user = updatePassword(user, password);
        passwordService.deleteTokenForUser(user);
        return user;
    }

    public ArgoUser updatePassword(ArgoUser user, String password) {
        String decryptPassword = passwordService.decryptPassword(password);
        user.setPassword(HashUtil.sha256(decryptPassword));
        return userRepository.save(user);
    }

    @Transactional
    public void confirmUser(String uuid) {
        ArgoUser confirmedUser = authService.confirmUser(uuid);
        userRegistrationConfirmMailService.sendCompletionMail(confirmedUser);
    }
}
