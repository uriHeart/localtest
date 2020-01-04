package com.argo.common.domain.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorWorkplaceRepository extends JpaRepository<VendorWorkplace, Long> {
//    VendorWorkplace findVendorWorkplaceByVendorWorkplaceId(Long vendorWorkplaceId);
//    List<VendorWorkplace>
    VendorWorkplace findByVendorOrderByType(Vendor vendor);
    List<VendorWorkplace> findAllByVendorAndDeletedIsFalseOrderByCreatedAtDesc(Vendor vendor);
    VendorWorkplace findByVendorWorkplaceId(Long vendorWorkplaceId);
    Boolean existsVendorWorkplaceByHashCodeEqualsAndDeletedIsFalse(Integer hashCode);
}
