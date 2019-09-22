package com.argo.common.domain.order.vendoritem;

import com.datastax.driver.core.DataType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@Table("external_vendor_item_mapping")
public class ExternalVendorItemMapping {
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "vendor_id")
    @CassandraType(type = DataType.Name.BIGINT)
    private Long vendorId;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.PARTITIONED, name = "source_item_id")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceItemId;

    @PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.PARTITIONED, name = "source_item_name")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceItemName;

    @PrimaryKeyColumn(ordinal = 3, type = PrimaryKeyType.PARTITIONED, name = "source_item_option")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceItemOption;

    @Column("vendor_item_id")
    @CassandraType(type = DataType.Name.UUID)
    private UUID vendorItemId;

    @Column("created_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date createdAt;
}
