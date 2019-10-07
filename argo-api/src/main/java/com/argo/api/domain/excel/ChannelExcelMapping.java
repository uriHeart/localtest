package com.argo.api.domain.excel;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@IdClass(ChannelExcelMappingPk.class)
@Data
@Builder
@Entity
@Table(name = "channel_excel_mapping", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class ChannelExcelMapping implements SystemMetadata {

    @Id
    private Long salesChannelId;

    @Id
    private String factorId;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "sales_channel_id", referencedColumnName = "sales_channel_id", insertable = false, updatable = false),
            @JoinColumn(name = "factor_id", referencedColumnName = "factor_id", insertable = false, updatable = false)
    })
    private Collection<ExcelFactorInfo> excelFactorInfo ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
