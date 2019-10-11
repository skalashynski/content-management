package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.container.impl.PageContainer;
import com.productiveedge.content_mgmt_automation.entity.request.GenerateExcelReportRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;
import com.productiveedge.content_mgmt_automation.repository.impl.excel.PageExcelReportImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class GenerateExcelReportFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(GenerateExcelReportFlow.class);

    private final PageExcelReportImpl pageExcelRepositoryImpl;
    private final PageContainer pageContainer;


    public GenerateExcelReportFlow(GenerateExcelReportRequest request) {
        this.pageContainer = new PageContainer();
        this.pageExcelRepositoryImpl = new PageExcelReportImpl(request.getReportPath(), request.getReportSheetName());
    }

    @Override
    public void run() {
        logger.info("Generating report.");
        try {
            pageExcelRepositoryImpl.saveAll(new ArrayList<>(pageContainer.getCache().values()));
            logger.info("The report is created.");
        } catch (ExcelException e) {
            logger.error(e.getMessage());
        }
    }
}
