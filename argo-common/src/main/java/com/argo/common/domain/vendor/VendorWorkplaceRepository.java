package com.argo.common.domain.vendor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorWorkplaceRepository extends JpaRepository<VendorWorkplace, Long> {
    VendorWorkplace findVendorWorkplaceByVendorWorkplaceId(Long vendorWorkplaceId);
//    List<VendorWorkplace>
}
