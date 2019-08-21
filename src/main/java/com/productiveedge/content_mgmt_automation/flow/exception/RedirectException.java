package com.productiveedge.content_mgmt_automation.flow.exception;

public class RedirectException extends Exception {
    public RedirectException() {
    }

    public RedirectException(String message) {
        super(message);
    }

    public RedirectException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedirectException(Throwable cause) {
        super(cause);
    }

    public RedirectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
