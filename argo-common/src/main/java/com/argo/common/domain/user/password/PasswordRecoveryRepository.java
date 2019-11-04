package com.argo.common.domain.user.password;

import com.argo.common.domain.user.ArgoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {
    PasswordRecovery findFirstByArgoUserAndIsActive(ArgoUser user);

    List<PasswordRecovery> findAllByArgoUser(ArgoUser user);

    PasswordRecovery findByTokenAndIsActive(String token);

    Long countByArgoUser(ArgoUser user);

    void deleteAllByArgoUser(ArgoUser user);
}