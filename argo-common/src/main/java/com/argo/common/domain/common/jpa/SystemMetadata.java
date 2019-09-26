package com.argo.common.domain.common.jpa;

import java.util.Date;

public interface SystemMetadata extends java.io.Serializable {
    void setCreatedAt(final Date date);
    void setUpdatedAt(final Date date);
}
