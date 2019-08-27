package com.productiveedge.content_mgmt_automation.flow.exception;

public class TakeScreenshotException extends Exception {
    public TakeScreenshotException() {
    }

    public TakeScreenshotException(String message) {
        super(message);
    }

    public TakeScreenshotException(String message, Throwable cause) {
        super(message, cause);
    }

    public TakeScreenshotException(Throwable cause) {
        super(cause);
    }

    public TakeScreenshotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
