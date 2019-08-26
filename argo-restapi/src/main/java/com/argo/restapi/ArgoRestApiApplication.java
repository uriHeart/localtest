package com.argo.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, CassandraAutoConfiguration.class})
//@Import({ArgoCommonConfig.class})
public class ArgoRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArgoRestApiApplication.class, args);
    }
}
