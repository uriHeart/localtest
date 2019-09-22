package com.argo.common.domain.order;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CassandraRepository<ArgoOrder, Long> {
}
