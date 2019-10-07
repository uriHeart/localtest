package com.argo.api.domain.excel;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ExcelFactorInfoPk implements Serializable {

    @Id
    @Column(name = "sales_channel_id", nullable = false)
    private Long salesChannelId;

    @Id
    @Column(name="factor_id")
    private String factorId;

    @Id
    @Column(name="column_no")
    private Integer columnNo;
}
