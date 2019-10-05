package com.argo.common.domain.vendor;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "vendor_channels", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class VendorChannel implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="vendor_channel_seq")
    @SequenceGenerator(name="vendor_channel_seq", sequenceName="vendor_channel_seq", allocationSize=1)
    @Column(name = "vendor_channel_id", nullable = false)
    private Long vendorChannelId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="sales_channel_id")
    private SalesChannel salesChannel;

    @Column(name = "enabled")
    private Boolean enabled; //boolean 으로 변경

    @Column(name = "auto_collecting")
    private Boolean autoCollecting;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
