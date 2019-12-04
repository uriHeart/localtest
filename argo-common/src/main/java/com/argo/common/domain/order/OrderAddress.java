package com.argo.common.domain.order;

import com.argo.common.domain.common.data.TargetData;
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
@AllArgsConstructor
@Table("order_address")
public class OrderAddress extends TargetData {

    public OrderAddress() {
        this.createdAt = new Date();
    }

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
    private String originalPostalCode; //dataRows.recv_zip

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

    public OriginalAddress getOriginalAddress() {
        if (this.originalAddress == null) {
            return OriginalAddress.builder().build();
        }

        return JsonUtil.read(this.originalAddress, OriginalAddress.class);
    }

    public void setOriginalAddress(OriginalAddress originalAddress) {
        this.originalAddress = JsonUtil.write(originalAddress);
    }

    public RefinedAddress getRefinedAddress() {
        if (this.refinedAddress == null) {
            return RefinedAddress.builder().build();
        }

        return JsonUtil.read(this.refinedAddress, RefinedAddress.class);
    }

    public void setRefinedAddress(RefinedAddress refinedAddress) {
        this.refinedAddress = JsonUtil.write(refinedAddress);
    }


    public Orderer getOrderer() {
        if (this.orderer == null) {
            return Orderer.builder().build();
        }

        return JsonUtil.read(this.orderer, Orderer.class);
    }

    public void setOrderer(Orderer orderer) {
        this.orderer = JsonUtil.write(orderer);
    }

    public Recipient getRecipient() {
        if (this.recipient == null) {
            return Recipient.builder().build();
        }

        return JsonUtil.read(this.recipient, Recipient.class);
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = JsonUtil.write(recipient);
    }

    public DeliveryRequest getDeliveryRequest() {
        if (this.deliveryRequest == null) {
            return DeliveryRequest.builder().build();
        }

        return JsonUtil.read(this.deliveryRequest, DeliveryRequest.class);
    }

    public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.deliveryRequest = JsonUtil.write(deliveryRequest);
    }
}
