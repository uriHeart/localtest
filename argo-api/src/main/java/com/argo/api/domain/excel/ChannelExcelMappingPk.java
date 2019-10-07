package com.argo.api.domain.excel;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ChannelExcelMappingPk implements Serializable {


    @EqualsAndHashCode.Include
    @Id
    @Column(name = "sales_channel_id", nullable = false)
    private Long salesChannelId;

    @EqualsAndHashCode.Include
    @Id
    @Column(name="factor_id")
    private String factorId;

}
