package com.argo.common.domain.common.jpa;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class UpdatedAtListener {
    @PreUpdate
    @PrePersist
    public void setUpdatedAt(final SystemMetadata entity) {
        entity.setUpdatedAt(new Date());
    }
}
