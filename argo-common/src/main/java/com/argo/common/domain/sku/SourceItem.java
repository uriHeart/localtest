package com.argo.common.domain.sku;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import com.argo.common.domain.vendor.Vendor;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "source_item_info", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class SourceItem implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="source_item_seq")
    @SequenceGenerator(name="source_item_seq", sequenceName="source_item_seq", allocationSize=1)
    @Column(name = "source_item_id", nullable = false)
    private Long sourceItemId;

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

    @Column(name = "barcode")
    private String barcode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
