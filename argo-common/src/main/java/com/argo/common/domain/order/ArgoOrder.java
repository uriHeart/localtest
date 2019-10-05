package com.argo.common.domain.order;

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

@Data
@Builder
@Table("argo_order")
@NoArgsConstructor
@AllArgsConstructor
public class ArgoOrder {
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

    @Column("paid_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date paidAt;

    @Column("metadata")
    @CassandraType(type = DataType.Name.TEXT)
    private String metadata;

    @Column("state")
    @CassandraType(type = DataType.Name.TEXT)
    private String state;

    @Column("event")
    @CassandraType(type = DataType.Name.TEXT)
    private String event;

    @Column("tracking_log")
    @CassandraType(type = DataType.Name.TEXT)
    private String trackingLog;

    @Column("replay_count")
    @CassandraType(type = DataType.Name.INT)
    private Integer replayCount;

    @Column("created_at")
    @CassandraType(type = DataType.Name.TIMESTAMP)
    private Date createdAt;

    public OrderMetadata getMetadata() {
        if (this.metadata == null) {
            return OrderMetadata.builder().build();
        }
        return JsonUtil.read(this.metadata, OrderMetadata.class);
    }

    public void setMetadata(OrderMetadata metadata) {
        this.metadata = JsonUtil.write(metadata);
    }
}
