package com.argo.common.domain.order;

import com.datastax.driver.core.DataType;
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
@Table("order_address")
public class OrderAddress {
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "vendor_id")
    @CassandraType(type = DataType.Name.BIGINT)
    private Long vendorId;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.PARTITIONED, name = "channel_id")
    @CassandraType(type = DataType.Name.BIGINT)
    private Long channelId;

    @PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.PARTITIONED, name = "order_id")
    @CassandraType(type = DataType.Name.TEXT)
    private String orderId;

    @PrimaryKeyColumn(ordinal = 3, type = PrimaryKeyType.CLUSTERED, name = "published_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date publishedAt;

    @Column("original_address")
    @CassandraType(type = DataType.Name.TEXT)
    private String originalAddress;

    @Column("original_postalcode")
    @CassandraType(type = DataType.Name.TEXT)
    private String originalPostalCode;

    @Column("refined_address")
    @CassandraType(type = DataType.Name.TEXT)
    private String refinedAddress;

    @Column("refined_postalcode")
    @CassandraType(type = DataType.Name.TEXT)
    private String refinedPostalCode;

    @Column("orderer")
    @CassandraType(type = DataType.Name.TEXT)
    private String orderer;

    @Column("recipient")
    @CassandraType(type = DataType.Name.TEXT)
    private String recipient;

    @Column("delivery_request")
    @CassandraType(type = DataType.Name.TEXT)
    private String deliveryRequest;

    @Column("created_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date createdAt;

}
