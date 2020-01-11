package com.argo.common.domain.vendor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Builder
public class EnumElem {
    VendorWorkplaceType key;
    String text;
    Long value;
    EnumElem(VendorWorkplaceType key, String text, Long value) {
        this.key = key;
        this.text = text;
        this.value = value;
    }
}
