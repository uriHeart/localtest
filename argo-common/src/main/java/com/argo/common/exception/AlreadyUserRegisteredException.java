package com.argo.common.exception;

public class AlreadyUserRegisteredException extends RuntimeException {

    public AlreadyUserRegisteredException(String message) {
        super(message);
    }
}
