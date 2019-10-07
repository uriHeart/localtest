package com.argo.common.domain.common.data.conversion.template;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionTemplateRepository extends CassandraRepository<ConversionTemplate, MapId> {
    List<ConversionTemplate> findAllBySourceId(String sourceId);
    ConversionTemplate findFirstBySourceIdAndTargetIdOrderByExpiredAtDesc(String sourceId, String targetId);

}