package com.argo.restapi.exception;

public class AlreadyUserRegisteredException extends RuntimeException {

    public AlreadyUserRegisteredException(String message) {
        super(message);
    }
}
