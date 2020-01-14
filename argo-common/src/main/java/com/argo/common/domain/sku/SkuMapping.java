package com.argo.common.domain.sku;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import com.argo.common.domain.vendor.item.VendorItem;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "sku_mapping", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class SkuMapping implements SystemMetadata {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sku_seq")
    @SequenceGenerator(name="sku_mapping_seq", sequenceName="sku_mapping_seq", allocationSize=1)
    @Column(name = "sku_mapping_id", nullable = false)
    private Long skuMappingId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vendor_item_id")
    private VendorItem vendorItem;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sku_id")
    private Sku sku;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
