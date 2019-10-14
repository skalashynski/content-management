package com.productiveedge.content_mgmt_automation.report.exception;

public class ExcelReportException extends Exception {
    public ExcelReportException() {
    }

    public ExcelReportException(String message) {
        super(message);
    }

    public ExcelReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelReportException(Throwable cause) {
        super(cause);
    }

    public ExcelReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
