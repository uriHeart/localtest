package com.argo.restapi.exception;

public class DecryptFailException extends RuntimeException {

    public DecryptFailException(Exception e) {
        super(e);
    }
}
