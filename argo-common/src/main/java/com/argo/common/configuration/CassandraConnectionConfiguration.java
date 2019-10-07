package com.argo.common.configuration;

import com.datastax.driver.core.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableReactiveCassandraRepositories
public class CassandraConnectionConfiguration extends AbstractReactiveCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.password}")
    private String password;

    @Value("${spring.data.cassandra.consistency-level}")
    private String consistencyLevel;

    @Value("${spring.data.cassandra.schema-action}")
    public String schemaAction;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Bean
    public QueryLogger queryLogger(Cluster cluster) {
        QueryLogger queryLogger = QueryLogger.builder().build();
        cluster.register(queryLogger);
        return queryLogger;
    }

    @Bean
    public CassandraSessionFactoryBean session() {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName(keyspace);
        session.setConverter(converter());
        session.setSchemaAction(SchemaAction.valueOf(schemaAction));
        return session;
    }

    @Bean
    public CassandraConverter converter() {
        return new MappingCassandraConverter(mappingContext());
    }

    @Bean
    public CassandraMappingContext mappingContext() {
        CassandraMappingContext context = new CassandraMappingContext();
        context.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), keyspace));
        return context;
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        QueryOptions queryOptions = new QueryOptions();

        ConsistencyLevel level = Optional.ofNullable(consistencyLevel)
                .map(String::toUpperCase)
                .map(ConsistencyLevel::valueOf)
                .orElse(ConsistencyLevel.QUORUM);

        queryOptions.setConsistencyLevel(level);

        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactPoints);
        cluster.setPassword(password);
        cluster.setUsername(username);
        cluster.setQueryOptions(queryOptions);
        cluster.setSocketOptions(new SocketOptions().setConnectTimeoutMillis(1000));
        cluster.setJmxReportingEnabled(false);

        return cluster;
    }

    @Bean
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(schemaAction);
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspace);
        return Lists.newArrayList(specification);
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return Lists.newArrayList(DropKeyspaceSpecification.dropKeyspace(keyspace));
    }
}
