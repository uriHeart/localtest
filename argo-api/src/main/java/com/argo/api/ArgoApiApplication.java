package com.argo.api;

import com.argo.api.configuration.ArgoApiConfig;
import com.argo.common.configuration.ArgoCommonConfig;
import com.argo.common.configuration.ArgoServletRegistrationBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
@Import({ArgoCommonConfig.class})
public class ArgoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArgoApiApplication.class, args);
    }

    @Bean(name = "extApi")
    public ArgoServletRegistrationBean extApi() {
        return new ArgoServletRegistrationBean("apiServlet", ArgoApiConfig.class, "/ext-api/*");
    }
}
