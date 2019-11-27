package com.argo.common.domain.sku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuAttributeRepository extends JpaRepository<SkuAttribute, Long>, SkuAttributeRepositoryCustom {
}
