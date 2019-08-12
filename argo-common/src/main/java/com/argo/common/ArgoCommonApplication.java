package com.argo.common;

import com.argo.common.configuration.ArgoCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
@Import({
        ArgoCommonConfig.class
})
public class ArgoCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArgoCommonApplication.class, args);
    }

}