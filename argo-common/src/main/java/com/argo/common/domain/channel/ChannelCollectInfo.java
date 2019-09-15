package com.argo.common.domain.channel;

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
@Table(name = "channel_collect_info", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class ChannelCollectInfo implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="channel_collect_info_seq")
    @SequenceGenerator(name="channel_collect_info_seq", sequenceName="channel_collect_info_seq", allocationSize=1)
    @Column(name = "channel_collect_info_id", nullable = false)
    private Long channelCollectInfoId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="sales_channel_id")
    private SalesChannel salesChannel;

    @Column(name = "collect_uri")
    private String collectUri;

    @Column(name = "collect_param")
    private String collectParam;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
