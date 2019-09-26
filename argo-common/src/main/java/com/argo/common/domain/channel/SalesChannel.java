package com.argo.common.domain.channel;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "sales_channels", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class SalesChannel implements SystemMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sales_channels_seq")
    @SequenceGenerator(name="sales_channels_seq", sequenceName="sales_channels_seq", allocationSize=1)
    @Column(name = "sales_channel_id", nullable = false)
    private Long salesChannelId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "base_uri")
    private String baseUrl;

    @Column(name = "token_uri")
    private String tokenUrl;

    @Column(name = "login_uri")
    private String loginUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
