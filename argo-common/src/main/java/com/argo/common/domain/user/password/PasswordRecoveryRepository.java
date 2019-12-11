package com.argo.common.domain.user.password;

import com.argo.common.domain.user.ArgoUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {
    PasswordRecovery findFirstByArgoUserAndActive(ArgoUser user, Boolean active);

    List<PasswordRecovery> findAllByArgoUser(ArgoUser user);

    PasswordRecovery findByTokenAndActive(String token, Boolean active);

    Long countByArgoUser(ArgoUser user);

    void deleteAllByArgoUser(ArgoUser user);

}
