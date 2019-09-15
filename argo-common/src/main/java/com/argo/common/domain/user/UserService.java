package com.argo.common.domain.user;

import com.argo.restapi.auth.RsaDecrypter;
import com.argo.restapi.exception.AlreadyUserRegisteredException;
import com.argo.restapi.util.HashUtil;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RsaDecrypter rsaDecrypter;

    @Autowired
    private HttpSession httpSession;

    public ArgoUser getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public ArgoUser addSeller(AddUserForm addUserForm) {
        Seller seller = addUserForm.toSellerEntity();
        String password = rsaDecrypter.decryptRsa(seller.getPassword(), httpSession.getAttribute("_RSA_WEB_Key_").toString());
        seller.setPassword(HashUtil.sha256(password));
        if (userRepository.existsByLoginId(seller.getLoginId())) {
            throw new AlreadyUserRegisteredException("이미 등록된 아이디 입니다.");
        }
        return userRepository.save(seller);
    }
}
