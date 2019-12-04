package com.argo.common.domain.common.data.conversion.template.reactive;

import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Repository
public interface ReactiveConversionTemplateRepository { //extends ReactiveCassandraRepository<ConversionTemplate, Long> {
    Flux<ConversionTemplate> findAllBySourceId(String sourceId);
    Mono<ConversionTemplate> findFirstBySourceIdAndTargetIdOrderByExpiredAtDesc(String sourceId, String targetId);
}
