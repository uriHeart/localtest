package com.argo.common.exception;

public class DecryptFailException extends RuntimeException {

    public DecryptFailException(Exception e) {
        super(e);
    }
}
