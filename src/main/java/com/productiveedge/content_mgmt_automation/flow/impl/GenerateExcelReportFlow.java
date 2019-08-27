package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.response.ExcelReportResponse;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import com.productiveedge.content_mgmt_automation.repository.PageExcelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateExcelReportFlow implements Flow<ExcelReportResponse> {
    private static final Logger logger = LoggerFactory.getLogger(GenerateExcelReportFlow.class);

    private final PageExcelProvider pageExcelProvider = new PageExcelProvider();

    @Override
    public ExcelReportResponse run() throws InvalidJarRequestException {
        logger.info("Generating report.");
        pageExcelProvider.saveAll(PageContainer.getCache().values());
        logger.info("The report is created.");
        return null;
    }
}
