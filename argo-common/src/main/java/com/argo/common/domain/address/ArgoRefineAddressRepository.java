package com.argo.common.domain.address;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ArgoRefineAddressRepository extends CassandraRepository<ArgoRefineAddress, String> {
    ArgoRefineAddress findByOriginalAddressHashAndRefineDate(String originalAddressHash, Date refineDate);
}
