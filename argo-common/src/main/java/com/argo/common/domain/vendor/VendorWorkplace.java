package com.argo.common.domain.vendor;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import com.argo.common.domain.user.UserStatus;
import com.argo.common.domain.vendor.Vendor;
import com.datastax.driver.core.DataType;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.CassandraType;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_workplace_vendor_workplace_id_seq")
    @SequenceGenerator(name = "vendor_workplace_vendor_workplace_id_seq", sequenceName = "vendor_workplace_vendor_workplace_id_seq", allocationSize = 1)
    @Column(name = "vendor_workplace_id", nullable = false)
    private Long vendorWorkplaceId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

//    @Column(name = "type")
//    private String type;
    //enum

    @Enumerated(EnumType.STRING)
    private VendorWorkplaceType type;

    @Column(name = "etc_type")
    private String etcType;

    @Column(name = "workplace_name")
    private String workplaceName;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "jibun_address")
    private String jibunAddress;

    @Column(name = "jibun_address_english")
    private String jibunAddressEnglish;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "road_address_english")
    private String roadAddressEnglish;

    @Column(name = "post_code_6_digits")
    private String postCode;

    @Column(name = "zip_code_5_digits")
    private String zipCode;

    @Column(name = "national_info")
    private String nationlInfo;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}