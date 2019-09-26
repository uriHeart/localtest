package com.argo.common.domain.vendor;

import com.argo.common.domain.channel.SalesChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelVendorAccountRepository extends JpaRepository<ChannelVendorAccount, Long> {
    ChannelVendorAccount findBySalesChannelAndVendor(SalesChannel salesChannel, Vendor vendor);
}
