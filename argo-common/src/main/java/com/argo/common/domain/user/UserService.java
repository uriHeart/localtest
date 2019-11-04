package com.argo.common.domain.user;

import com.argo.common.domain.auth.HashUtil;
import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.domain.user.password.PasswordService;
import com.argo.common.exception.AlreadyUserRegisteredException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ArgoUser addSeller(AddUserForm addUserForm, String rsaPrivateKey) {
        Seller seller = addUserForm.toSellerEntity();
        String password = rsaDecrypter.decryptRsa(seller.getPassword(), rsaPrivateKey);
        seller.setPassword(HashUtil.sha256(password));
        if (userRepository.existsByLoginId(seller.getLoginId())) {
            throw new AlreadyUserRegisteredException("이미 등록된 아이디 입니다.");
        }
        return userRepository.save(seller);
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
