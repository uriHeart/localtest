package com.argo.common.domain.sku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuMappingRepository extends JpaRepository<SkuMapping, Long>, SkuMappingRepositoryCustom {
}
