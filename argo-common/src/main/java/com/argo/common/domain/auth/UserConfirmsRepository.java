package com.argo.common.domain.auth;

import com.argo.common.domain.user.ArgoUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfirmsRepository extends JpaRepository<UserConfirm, Long> {
    Optional<UserConfirm> findByArgoUser(ArgoUser user);
    UserConfirm findByUuid(String uuid);
}
