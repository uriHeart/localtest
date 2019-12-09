////package com.argo.common.domain.vendor;
////import com.argo.common.domain.common.jpa.CreatedAtListener;
////import com.argo.common.domain.common.jpa.SystemMetadata;
////import com.argo.common.domain.common.jpa.UpdatedAtListener;
////import lombok.*;
////
////import javax.persistence.*;
////import java.util.Date;
////
////@Data
////@Builder
////@Entity
////@Table(name = "vendor_workplace", schema = "public")
////@AllArgsConstructor(access = AccessLevel.PRIVATE)
////@NoArgsConstructor
////@ToString
////@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
////public class VendorWorkplace {
////    @Id
////    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_workplace_id")
////    @SequenceGenerator(name = "vendor_workplace_id", sequenceName = "vendor_workplace_id", allocationSize = 1)
////    @Column(name = "vendor_id", nullable = false)
////    private Long vendorWorkplaceId;
////
////    // vendor name 추가? vendorId 잇는거 사용하면 될?
////    @ManyToOne(fetch=FetchType.EAGER)
////    @JoinColumn(name="vendor_id")
//////    @Column(name = "vendor_office_address")
////    private String vendorOfficeAddress;
////
////    @ManyToOne(fetch=FetchType.EAGER)
////    @JoinColumn(name="vendor_id")
//////    @Column(name = "vendor_warehouse_address")
////    private String vendorWarehouseAddress;
////
////    @ManyToOne(fetch=FetchType.EAGER)
////    @JoinColumn(name="vendor_id")
//////    @Column(name = "vendor_store_address")
////    private String vendorStoreAddress;
//
////
////    @ManyToOne(fetch=FetchType.EAGER)
////    @JoinColumn(name="vendor_id")
////    private String vendorNation;
//}