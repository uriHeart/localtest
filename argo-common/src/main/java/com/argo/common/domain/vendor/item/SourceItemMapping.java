package com.argo.common.domain.vendor.item;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import com.argo.common.domain.vendor.Vendor;
import com.google.common.hash.Hashing;
import lombok.*;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.StringJoiner;

@Data
@Builder
@Entity
@Table(name = "source_item_mapping", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class SourceItemMapping implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="source_item_mapping_seq")
    @SequenceGenerator(name="source_item_mapping_seq", sequenceName="source_item_mapping_seq", allocationSize=1)
    @Column(name = "source_item_mapping_id", nullable = false)
    private Long sourceItemMappingId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="channel_id")
    private SalesChannel salesChannel;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_option")
    private String itemOption;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vendor_item_id")
    private VendorItem vendorItem;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public String getSourceItemKey() {
        StringJoiner strJoiner = new StringJoiner("_");
        strJoiner.add(this.getItemName()).add(this.getItemOption());
        return Hashing.sha256().hashString(strJoiner.toString(), StandardCharsets.UTF_8).toString();
    }
}
