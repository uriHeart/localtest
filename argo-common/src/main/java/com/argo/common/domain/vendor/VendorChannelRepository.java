package com.argo.common.domain.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorChannelRepository extends JpaRepository<VendorChannel, Long> {
    List<VendorChannel> findByEnabled(Boolean enabled);
    List<VendorChannel> findAllByAutoCollecting(Boolean autoCollecting);
    List<VendorChannel> findByVendorAndEnabledAndAutoCollecting(Vendor vendor, Boolean enabled, Boolean autoCollecting);
}
