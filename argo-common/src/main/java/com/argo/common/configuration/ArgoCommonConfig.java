package com.argo.common.configuration;

import com.argo.common.ArgoCommon;
import com.argo.common.configuration.database.ArgoPostgreSqlConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;

@Configuration
@ComponentScan(basePackageClasses = {ArgoCommon.class})
@Import({
        ArgoPostgreSqlConfig.class
})
public class ArgoCommonConfig implements TransactionManagementConfigurer {

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
