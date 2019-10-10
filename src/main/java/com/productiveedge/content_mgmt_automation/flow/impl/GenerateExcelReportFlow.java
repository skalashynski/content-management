package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.request.GenerateExcelReportRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;
import com.productiveedge.content_mgmt_automation.repository.impl.PageExcelRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class GenerateExcelReportFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(GenerateExcelReportFlow.class);

    private final PageExcelRepositoryImpl pageExcelRepositoryImpl;
    private final GenerateExcelReportRequest request;

    public GenerateExcelReportFlow(GenerateExcelReportRequest request) {
        this.request = request;
        this.pageExcelRepositoryImpl = new PageExcelRepositoryImpl(this.request.getReportPath(), this.request.getReportSheetName());
    }

    @Override
    public void run() {
        logger.info("Generating report.");
        try {
            pageExcelRepositoryImpl.saveAll(PageContainer.getCache().values().stream().collect(Collectors.toList()));
            logger.info("The report is created.");
        } catch (ExcelException e) {
            logger.error(e.getMessage());
        }
    }
}
