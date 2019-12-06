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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_workplace_id")
    @SequenceGenerator(name = "vendor_workplace_id", sequenceName = "vendor_workplace_id", allocationSize = 1)
    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;
    // vendor name 추가? vendorId 잇는거 사숑하면 될거 같기도 한데??
    @Column(name = "vendor_office_address")
    private String vendorOfficeAddress;

    @Column(name = "vendor_store_address")
    private String vendorStoreAddress;

    @Column(name = "vendor_warehouse_address")
    private String vendorWarehouseAddress;

    @Column(name = "vendor_national_info")
    private String vendorNationalInfo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}