package com.argo.common.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableJpaRepositories(basePackages = "com.argo")
@EnableJpaAuditing
@Import({ArgoPostgreSqlConfig.class, CassandraConnectionConfiguration.class})
@EntityScan(basePackages = "com.argo")
@EnableAsync(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.argo"})
public class ArgoCommonConfig implements AsyncConfigurer, TransactionManagementConfigurer {

    @Resource
    private PlatformTransactionManager transactionManager;

    @Bean
    public PlatformTransactionManager chainedTransactionManager() {
        return new ChainedTransactionManager(transactionManager);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return chainedTransactionManager();
    }
}
