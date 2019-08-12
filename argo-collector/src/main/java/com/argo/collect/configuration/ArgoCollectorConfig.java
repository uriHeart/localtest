package com.argo.collect.configuration;

import com.argo.collect.ArgoCollector;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {ArgoCollector.class})
public class ArgoCollectorConfig {
}

