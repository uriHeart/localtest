package com.argo.common.domain.common.jpa;

import javax.persistence.PrePersist;
import java.util.Date;

public class CreatedAtListener {
    @PrePersist
    public void setCreatedAt(final SystemMetadata entity) {
        entity.setCreatedAt(new Date());
    }
}
