package com.argo.common.domain.sku;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "sku_master", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class Sku implements SystemMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sku_seq")
    @SequenceGenerator(name="sku_seq", sequenceName="sku_seq", allocationSize=1)
    @Column(name = "sku_id", nullable = false)
    private Long skuId;

    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "length", nullable = false)
    private Double length;

    @Column(name = "width", nullable = false)
    private Double width;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="parent_sku_id")
    private Sku parentSku;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "brand")
    private String brand;

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
