package com.argo.common.domain.user;

import com.argo.common.domain.auth.HashUtil;
import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.exception.AlreadyUserRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RsaDecrypter rsaDecrypter;

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
}
