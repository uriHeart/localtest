package com.argo.common.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableJpaRepositories(basePackages = "com.argo.common.domain.jpa")
@EnableCassandraRepositories(basePackages = "com.argo.common.domain.cassandra")
@EnableJpaAuditing
@EntityScan(basePackages = "com.argo.common")
@EnableAsync(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.argo"})
public class ArgoCommonConfig implements AsyncConfigurer {
}
