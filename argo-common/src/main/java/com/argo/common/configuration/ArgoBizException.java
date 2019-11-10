package com.argo.common.configuration;

public class ArgoBizException extends RuntimeException {
    public ArgoBizException() {
        super();
    }

    public ArgoBizException(String message) {
        super(message);
    }

    public ArgoBizException(String message, Throwable e) {
        super(message, e);
    }
}
