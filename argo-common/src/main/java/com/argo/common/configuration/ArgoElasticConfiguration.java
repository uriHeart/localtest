package com.argo.common.configuration;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ArgoElasticConfiguration {

    @Bean
    public RestHighLevelClient client(ElasticsearchProperties properties) {
        return new RestHighLevelClient(
                RestClient.builder(properties.hosts())
        );
    }
}
