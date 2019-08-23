package com.productiveedge.content_mgmt_automation.flow.exception;

public class InvalidHrefException extends Exception {
    public InvalidHrefException() {
    }

    public InvalidHrefException(String message) {
        super(message);
    }

    public InvalidHrefException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHrefException(Throwable cause) {
        super(cause);
    }

    public InvalidHrefException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
