package com.argo.api.domain.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ManualOrderPk implements Serializable {

    @Id
    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Id
    @Column(name="file_name")
    private String fileName;
}
