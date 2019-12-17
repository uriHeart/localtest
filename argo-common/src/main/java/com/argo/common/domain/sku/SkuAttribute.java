package com.argo.common.domain.sku;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "sku_attribute", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class SkuAttribute implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sku_attribute_seq")
    @SequenceGenerator(name="sku_attribute_seq", sequenceName="sku_attribute_seq", allocationSize=1)
    @Column(name = "sku_attribute_id", nullable = false)
    private Long skuAttributeId;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name="sku_id", nullable = false)
    @ToString.Exclude
    private Sku sku;

    @Column(name = "sku_id", nullable = false, insertable = false, updatable = false)
    private Long skuId;

    @Column(name = "attribute_key")
    private String attributeKey;

    @Column(name = "attribute_value")
    private String attributeValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "created_by")
    private Date createdBy;

    @Column(name = "updated_by")
    private Date updatedBy;
}
