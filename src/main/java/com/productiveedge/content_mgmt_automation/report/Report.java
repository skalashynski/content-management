package com.productiveedge.content_mgmt_automation.report;

import com.productiveedge.content_mgmt_automation.report.exception.ExcelReportException;

public interface Report<T> {
    void saveAll(T elements) throws ExcelReportException;
}
