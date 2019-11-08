package com.argo.common.domain.auth;

import com.argo.common.domain.user.ArgoUser;
import com.argo.common.exception.UserRegistrationException;
import java.util.UUID;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final int TTL_HOURS = 2;

    @Autowired
    private UserConfirmsRepository userConfirmsRepository;

    @Transactional
    public String createUuidForConfirm(ArgoUser user) {
        if (user.isApproved()) {
            throw new UserRegistrationException("이미 승인된 사용자 입니다.");
        }

        String uuid = UUID.randomUUID().toString();
        UserConfirm userConfirm = userConfirmsRepository.findByArgoUser(user).orElse(UserConfirm.builder().argoUser(user).build());
        userConfirm.setTtl(LocalDateTime.now().plusHours(TTL_HOURS).toDate());
        userConfirm.setUuid(uuid);

        return userConfirmsRepository.save(userConfirm).getUuid();
    }

    @Transactional
    public ArgoUser confirmUser(String uuid) {
        UserConfirm confirm = userConfirmsRepository.findByUuid(uuid);
        confirm.userRegistrationConfirm();

        return confirm.getArgoUser();
    }
}
