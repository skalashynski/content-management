package com.productiveedge.content_mgmt_automation.flow.exception;

public class HttpRedirectException extends Exception {
    public HttpRedirectException() {
    }

    public HttpRedirectException(String message) {
        super(message);
    }

    public HttpRedirectException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRedirectException(Throwable cause) {
        super(cause);
    }

    public HttpRedirectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
