package com.argo.common.domain.auth;

import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.Seller;
import com.argo.common.exception.UserAlreadyApprovedException;
import java.util.UUID;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserConfirmsRepository userConfirmsRepository;

    public String createUuidForConfirm(Seller user) {
        if (user.isApproved()) {
            throw new UserAlreadyApprovedException("이미 승인된 사용자 입니다.");
        }

        String uuid = UUID.randomUUID().toString();
        UserConfirm userConfirm = userConfirmsRepository.findByArgoUser(user).orElse(UserConfirm.builder().argoUser(user).build());
        userConfirm.setTtl(LocalDateTime.now().toDate());
        userConfirm.setUuid(uuid);

        userConfirmsRepository.save(userConfirm);
        return uuid;
    }

    @Transactional
    public void confirmUser(String uuid) {
        UserConfirm byUuid = userConfirmsRepository.findByUuid(uuid);
        ArgoUser user = byUuid.getArgoUser();
        user.setApproved(true);
    }
}
