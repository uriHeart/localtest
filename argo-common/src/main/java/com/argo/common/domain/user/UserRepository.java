package com.argo.common.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ArgoUser, Long> {
    ArgoUser findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
