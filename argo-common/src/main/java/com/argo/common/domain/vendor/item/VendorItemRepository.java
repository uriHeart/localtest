package com.argo.common.domain.vendor.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorItemRepository extends JpaRepository<VendorItem, Long> {
}
