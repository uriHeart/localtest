package com.argo.common.domain.common.data;

public interface Converter<Source, Target> {
    Target convert(Source sourceObject);
}