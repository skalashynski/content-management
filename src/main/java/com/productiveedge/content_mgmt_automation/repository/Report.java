package com.productiveedge.content_mgmt_automation.repository;

import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;

public interface Report<T> {
    void saveAll(T elements) throws ExcelException;
}
