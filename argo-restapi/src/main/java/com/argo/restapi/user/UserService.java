package com.argo.restapi.user;

import com.argo.restapi.exception.AlreadyUserRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ArgoUser getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public ArgoUser addSeller(AddUserForm addUserForm) {
        Seller seller = addUserForm.toSellerEntity();
        if (userRepository.existsByLoginId(seller.getLoginId())) {
            throw new AlreadyUserRegisteredException("이미 등록된 아이디 입니다.");
        }
        return userRepository.save(seller);
    }
}
