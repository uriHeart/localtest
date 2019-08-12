package com.argo.collect;

import com.argo.collect.configuration.ArgoCollectorConfig;
import com.argo.common.configuration.ArgoCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
@Import({
        ArgoCommonConfig.class, ArgoCollectorConfig.class
})
public class ArgoCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArgoCollectorApplication.class, args);
    }

}