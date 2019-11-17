
package com.argo.api.service.gopotal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bzowrRgstNo",
    "dataCrtYm",
    "ldongAddrMgplDgCd",
    "ldongAddrMgplSgguCd",
    "ldongAddrMgplSgguEmdCd",
    "seq",
    "wkplJnngStcd",
    "wkplNm",
    "wkplRoadNmDtlAddr",
    "wkplStylDvcd"
})
public class Item {

    private String bzowrRgstNo;
    private Integer dataCrtYm;
    private Integer ldongAddrMgplDgCd;
    private Integer ldongAddrMgplSgguCd;
    private Integer ldongAddrMgplSgguEmdCd;
    private Integer seq;
    private Integer wkplJnngStcd;
    private String wkplNm;
    private String wkplRoadNmDtlAddr;
    private Integer wkplStylDvcd;

}
