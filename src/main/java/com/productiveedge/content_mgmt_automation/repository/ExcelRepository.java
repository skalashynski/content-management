package com.productiveedge.content_mgmt_automation.repository;

import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;

import java.util.Collection;

public interface ExcelRepository<T> {
    void saveAll(Collection<T> elements) throws ExcelException;
}
