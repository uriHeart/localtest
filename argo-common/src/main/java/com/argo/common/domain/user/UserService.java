package com.argo.common.domain.user;

import com.argo.common.domain.auth.HashUtil;
import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.domain.user.password.PasswordService;
import com.argo.common.exception.AlreadyUserRegisteredException;
import lombok.AllArgsConstructor;
import com.argo.common.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private RsaDecrypter rsaDecrypter;

    private PasswordService passwordService;

    public ArgoUser getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Transactional
    public ArgoUser addSeller(AddUserForm addUserForm, String rsaPrivateKey) {
        assertValid(addUserForm);

        Seller seller = addUserForm.toSellerEntity();
        String password = rsaDecrypter.decryptRsa(seller.getPassword(), rsaPrivateKey);
        seller.setPassword(HashUtil.sha256(password));
        seller.setApproved(false);
        Seller save = userRepository.save(seller);
        signUpMailService.sendConfirmMail(save);
        return save;
    }

    private void assertValid(AddUserForm form) {
        if (userRepository.existsByLoginId(form.getEmail())) {
            throw new UserRegistrationException("이미 등록된 메일주소 입니다.");
        }
    }

    public ArgoUser updateUser(ArgoUser inputUser, String email) {
        ArgoUser user = userRepository.findByLoginId(email);
        // update user with not null values
        if(inputUser.getLoginId() != null) {
            user.setLoginId(inputUser.getLoginId());
        }

        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

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
}
