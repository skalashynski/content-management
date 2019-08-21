package com.productiveedge.content_mgmt_automation.flow.exception;

public class InvalidJarRequestException extends Exception {
    public InvalidJarRequestException() {
        super();
    }

    public InvalidJarRequestException(String message) {
        super(message);
    }

    public InvalidJarRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJarRequestException(Throwable cause) {
        super(cause);
    }

    protected InvalidJarRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
