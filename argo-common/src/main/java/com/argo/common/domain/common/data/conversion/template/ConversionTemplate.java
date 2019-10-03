package com.argo.common.domain.common.data.conversion.template;

import com.datastax.driver.core.DataType;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Table("conversion_template")
public class ConversionTemplate {
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "source_id")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceId;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, name = "target_id")
    @CassandraType(type = DataType.Name.TEXT)
    private String targetId;

    @PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.CLUSTERED, name = "created_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date createdAt;

    @PrimaryKeyColumn(ordinal = 3, type = PrimaryKeyType.CLUSTERED, name = "expired_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date expiredAt;

    @Column
    @CassandraType(type = DataType.Name.UDT, userTypeName = "conversion_rule")
    private List<ConversionRule> rules;
}
