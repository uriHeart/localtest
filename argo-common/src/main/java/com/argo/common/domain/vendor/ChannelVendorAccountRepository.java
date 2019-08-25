package com.argo.common.domain.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelVendorAccountRepository extends JpaRepository<ChannelVendorAccount, Long> {
}
