package com.argo.collect;

import com.argo.collect.configuration.ArgoCollectorApiConfig;
import com.argo.collect.configuration.ArgoCollectorWebConfig;
import com.argo.common.configuration.ArgoCommonConfig;
import com.argo.common.configuration.ArgoServletRegistrationBean;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = { FlywayAutoConfiguration.class })
@Import({ArgoCommonConfig.class})
public class ArgoCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArgoCollectorApplication.class, args);
    }

    @Bean(name = "web")
    public ArgoServletRegistrationBean web() {
        return new ArgoServletRegistrationBean("webServlet", ArgoCollectorWebConfig.class, "/web");
    }

//    @Bean(name = "api")
//    public ArgoServletRegistrationBean api() {
//        return new ArgoServletRegistrationBean("apiServlet", ArgoCollectorApiConfig.class, "/api/*");
//    }

}