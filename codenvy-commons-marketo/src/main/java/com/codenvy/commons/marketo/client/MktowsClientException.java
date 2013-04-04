package com.codenvy.commons.marketo.client;

public class MktowsClientException extends Exception {

    public MktowsClientException() {
        super();
    }

    public MktowsClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MktowsClientException(String message) {
        super(message);
    }

    public MktowsClientException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 1L;
}
