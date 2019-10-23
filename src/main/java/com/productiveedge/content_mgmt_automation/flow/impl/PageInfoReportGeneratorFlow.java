package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.request.GenerateExcelReportRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.report.exception.ReportException;
import com.productiveedge.content_mgmt_automation.report.impl.excel.PageInfoExcelReportImpl;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PageInfoReportGeneratorFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(PageInfoReportGeneratorFlow.class);

    private final PageInfoExcelReportImpl pageExcelRepositoryImpl;
    private final PageContainer pageContainer;


    public PageInfoReportGeneratorFlow(GenerateExcelReportRequest request) {
        this.pageContainer = PageContainer.getInstance();
        this.pageExcelRepositoryImpl = new PageInfoExcelReportImpl(request.getReportPath(), request.getReportSheetName());
    }

    @Override
    public void run() {
        logger.info("Generating excel report........");
        try {
            pageExcelRepositoryImpl.saveAll(new ArrayList<>(pageContainer.getCache().values()));
            logger.info("The data is saved to the excel file.");
        } catch (ReportException e) {
            logger.error(e.getMessage());
        }
    }
}
