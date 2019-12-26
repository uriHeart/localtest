package com.argo.api.domain.excel;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@IdClass(ManualOrderPk.class)
@Data
@Builder
@Entity
@Table(name = "manual_order", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
@ToString
public class ManualOrder implements SystemMetadata {

    @Id
    private Long vendorId;

    @Id
    private String fileName;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "status")
    private String status;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}
