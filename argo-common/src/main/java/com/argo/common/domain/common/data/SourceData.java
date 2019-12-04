package com.argo.common.domain.common.data;

public abstract class SourceData {
    public abstract String sourceKey();
    public String className() {
        return this.getClass().getName();
    }
}
