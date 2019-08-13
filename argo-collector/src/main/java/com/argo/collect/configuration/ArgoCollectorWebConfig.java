package com.argo.collect.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackages = "com.argo.collect.web")
public class ArgoCollectorWebConfig extends WebMvcConfigurationSupport {
}

