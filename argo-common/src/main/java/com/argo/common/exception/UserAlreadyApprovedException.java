package com.argo.common.exception;

public class UserAlreadyApprovedException extends RuntimeException {
    public UserAlreadyApprovedException(String message) {
        super(message);
    }
}
