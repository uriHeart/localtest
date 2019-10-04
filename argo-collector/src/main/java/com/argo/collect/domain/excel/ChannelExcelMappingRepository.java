package com.argo.collect.domain.excel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelExcelMappingRepository extends JpaRepository<ChannelExcelMapping, Long> {

    List<ChannelExcelMapping> findBySalesChannelId(Long salesChannelId);
}
