package com.argo.address;

import com.argo.address.configuration.ArgoAddressApiConfig;
import com.argo.common.configuration.ArgoCommonConfig;
import com.argo.common.configuration.ArgoServletRegistrationBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication(exclude = { FlywayAutoConfiguration.class })
@Import({ArgoCommonConfig.class})
public class ArgoAddressApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArgoAddressApplication.class, args);
    }

    @EnableJdbcHttpSession
    public class HttpSessionConfig {
    }

    @Bean(name = "api")
    public ArgoServletRegistrationBean api() {
        return new ArgoServletRegistrationBean("apiServlet", ArgoAddressApiConfig.class, "/api/*");
    }

}