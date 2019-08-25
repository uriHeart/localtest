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
@Table(name = "channel_vendor_accounts", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class ChannelVendorAccount implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="vendors_seq")
    @SequenceGenerator(name="vendors_seq", sequenceName="vendors_seq", allocationSize=1)
    @Column(name = "channel_vendor_account_id", nullable = false)
    private Long channelVendorAccountId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sales_channel_id")
    private SalesChannel salesChannel;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    @Column(name = "credential_id")
    private String credentialId;

    @Column(name = "credential_password")
    private String credentialPassword;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
