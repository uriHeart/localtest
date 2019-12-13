package com.argo.common.domain.vendor;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "vendor_workplace", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class VendorWorkplace implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_workplace_id_seq")
    @SequenceGenerator(name = "vendor_workplace_id_seq", sequenceName = "vendor_workplace_id_seq", allocationSize = 1)
    @Column(name = "vendor_workplace_id", nullable = false)
    private Long vendorWorkplaceId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    @Column(name = "type")
    private String type;

    @Column(name = "vendor_office_address")
    private String vendorOfficeAddress;

    @Column(name = "vendor_warehouse_address")
    private String vendorWarehouseAddress;

    @Column(name = "vendor_store_address")
    private String vendorStoreAddress;

    @Column(name = "vendor_nation_info")
    private String vendorNation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}