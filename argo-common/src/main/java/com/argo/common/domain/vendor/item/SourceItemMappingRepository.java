package com.argo.common.domain.vendor.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceItemMappingRepository extends JpaRepository<SourceItemMapping, Long>, SourceItemMappingRepositoryCustom {
}
