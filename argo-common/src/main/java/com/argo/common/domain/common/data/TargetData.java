package com.argo.common.domain.common.data;

public abstract class TargetData {
    //public abstract String targetKey();
    public String className() {
        return this.getClass().getName();
    }
}
