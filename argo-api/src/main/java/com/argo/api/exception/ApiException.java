package com.argo.api.exception;

import java.util.function.Supplier;

public class ApiException extends RuntimeException implements Supplier<RuntimeException> {
    public ApiException(String message) {
        super(message);
    }

    @Override
    public RuntimeException get() {
        return this;
    }
}
