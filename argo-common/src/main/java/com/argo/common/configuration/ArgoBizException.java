package com.argo.common.configuration;

import java.util.function.Supplier;

public class ArgoBizException extends RuntimeException implements Supplier<RuntimeException> {
    public ArgoBizException() {
        super();
    }

    public ArgoBizException(String message) {
        super(message);
    }

    public ArgoBizException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public RuntimeException get() {
        return this;
    }
}
