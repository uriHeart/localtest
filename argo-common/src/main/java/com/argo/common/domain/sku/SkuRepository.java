package com.argo.common.domain.sku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long>, SkuRepositoryCustom {
//    public List<Sku> findByVendorId(Long vendorId);
}
