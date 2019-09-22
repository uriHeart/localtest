package com.argo.common.domain.order.vendoritem;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalVendorItemMappingRepository extends CassandraRepository<ExternalVendorItemMapping, Long> {
}
