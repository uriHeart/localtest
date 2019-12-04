package com.argo.common.domain.address;

import com.datastax.driver.core.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Table("argo_refine_address")
public class ArgoRefineAddress {
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "original_address_hash")
    @CassandraType(type = DataType.Name.TEXT)
    private String originalAddressHash;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, name = "refine_date")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date refineDate;

    @Column("original_address")
    @CassandraType(type = DataType.Name.TEXT)
    private String originalAddress;

    @Column("jibun_address")
    @CassandraType(type = DataType.Name.TEXT)
    private String jibunAddress;

    @Column("road_address")
    @CassandraType(type = DataType.Name.TEXT)
    private String roadAddress;

    @Column("latitude")
    @CassandraType(type = DataType.Name.DOUBLE)
    private Double latitude;

    @Column("longitude")
    @CassandraType(type = DataType.Name.DOUBLE)
    private Double longitude;
}
