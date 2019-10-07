package com.argo.api.domain.excel;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@IdClass(ExcelFactorInfoPk.class)
@Data
@Builder
@Entity
@Table(name = "excel_factor_info", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
@ToString
public class ExcelFactorInfo implements SystemMetadata {

    @Id
    private Long salesChannelId;

    @Id
    private String factorId;

    @Id
    private Integer columnNo;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
