package com.argo.common.domain.common.data;

public abstract class ConvertibleData {
    public abstract String sourceKey();
    public String className() {
        return this.getClass().getName();
    }
}
