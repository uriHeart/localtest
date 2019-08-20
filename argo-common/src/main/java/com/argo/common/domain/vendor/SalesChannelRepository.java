package com.argo.common.domain.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesChannelRepository extends JpaRepository<SalesChannel, Long> {
    SalesChannel findBySalesChannelId(Long salesChannelId);
    SalesChannel findByCode(String code);
}
