package com.productiveedge.content_mgmt_automation.service.exception;

public class ApacheHttpClientException extends Exception {
    public ApacheHttpClientException() {
    }

    public ApacheHttpClientException(String message) {
        super(message);
    }

    public ApacheHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApacheHttpClientException(Throwable cause) {
        super(cause);
    }

    public ApacheHttpClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
