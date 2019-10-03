package com.argo.common.cassandra;

public class CassandraPrimaryKeyExtractionException extends RuntimeException {
    public CassandraPrimaryKeyExtractionException(Throwable t) {super(t);}
    public CassandraPrimaryKeyExtractionException(String message, Throwable t) {super(message, t);}
    public CassandraPrimaryKeyExtractionException(String message) {super(message);}
}
