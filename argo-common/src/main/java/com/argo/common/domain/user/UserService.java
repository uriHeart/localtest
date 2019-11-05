package com.argo.common.domain.user;

import com.argo.common.domain.auth.HashUtil;
import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RsaDecrypter rsaDecrypter;

    @Autowired
    private SignUpMailService signUpMailService;

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
}
