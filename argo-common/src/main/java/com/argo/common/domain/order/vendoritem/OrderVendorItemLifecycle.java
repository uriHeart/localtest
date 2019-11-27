package com.argo.common.domain.order.vendoritem;

import com.argo.common.domain.common.util.JsonUtil;
import com.datastax.driver.core.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Table("order_vendor_item_lifecycle")
public class OrderVendorItemLifecycle {

    public OrderVendorItemLifecycle() {
        this.replayCount = 0;
        this.createdAt = new Date();
    }

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "vendor_id")
    @CassandraType(type = DataType.Name.BIGINT)
    private Long vendorId;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.PARTITIONED, name = "channel_id")
    @CassandraType(type = DataType.Name.BIGINT)
    private Long channelId;

    @PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.PARTITIONED, name = "order_id")
    @CassandraType(type = DataType.Name.VARCHAR)
    private String orderId;

    @PrimaryKeyColumn(ordinal = 3, type = PrimaryKeyType.CLUSTERED, name = "vendor_item_id")
    @CassandraType(type = DataType.Name.TEXT)
    private String vendorItemId;

    @PrimaryKeyColumn(ordinal = 4, type = PrimaryKeyType.CLUSTERED, name = "published_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date publishedAt;

    @Column("source_item_id")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceItemId; // dataRows.product_id

    @Column("source_item_name")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceItemName; // dataRows.product_name

    @Column("source_item_option")
    @CassandraType(type = DataType.Name.TEXT)
    private String sourceItemOption; // dataRows.options

    @Column("metadata")
    @CassandraType(type = DataType.Name.TEXT)
    private String metadata;

    @Column("state")
    @CassandraType(type = DataType.Name.TEXT)
    private String state;

    @Column("event")
    @CassandraType(type = DataType.Name.TEXT)
    private String event;

    @Column("sku_mappings")
    @CassandraType(type = DataType.Name.TEXT)
    private String skuMappings;

    @Column("quantity")
    @CassandraType(type = DataType.Name.INT)
    private Long quantity; //dataRows.qty

    @Column("replay_count")
    @CassandraType(type = DataType.Name.INT)
    private Integer replayCount;

    @Column("created_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date createdAt;

    public OrderVendorItemMetadata getMetadata() {
        if (this.metadata == null) {
            return OrderVendorItemMetadata.builder().build();
        }

        return JsonUtil.read(this.metadata, OrderVendorItemMetadata.class);
    }

    public void setMetadata(OrderVendorItemMetadata orderVendorItemMetadata) {
        this.metadata = JsonUtil.write(orderVendorItemMetadata);
    }

    public SkuMappingData getSkuMappings() {
        if (this.skuMappings == null) {
            return SkuMappingData.builder().build();
        }

        return JsonUtil.read(this.skuMappings, SkuMappingData.class);
    }

    public void setSkuMappings(SkuMappingData skuMappings) {
        this.skuMappings = JsonUtil.write(skuMappings);
    }
}
