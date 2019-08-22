package com.productiveedge.content_mgmt_automation.flow.exception;

public class ProcessPageException extends Exception {
    public ProcessPageException() {
        super();
    }

    public ProcessPageException(String message) {
        super(message);
    }

    public ProcessPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessPageException(Throwable cause) {
        super(cause);
    }

    protected ProcessPageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
