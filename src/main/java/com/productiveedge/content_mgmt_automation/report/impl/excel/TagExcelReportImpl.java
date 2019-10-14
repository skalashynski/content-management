package com.productiveedge.content_mgmt_automation.report.impl.excel;

import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.report.exception.ExcelReportException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagExcelReportImpl extends ExcelReport<List<Tag>> {
    private static final Logger logger = LoggerFactory.getLogger(TagExcelReportImpl.class);


    public TagExcelReportImpl(String xlsxReportFilePath, String sheetName) {
        super(xlsxReportFilePath, sheetName);
    }

    public List<String> getHeaderNames() {
        return Arrays.stream(BaseTag.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Tag> tags) throws ExcelReportException {
        List<String> columns = getHeaderNames();

        createColumnHeaders(columns);
        // Create Other rows and cells with page data
        int rowNum = 1;
        for (Tag tag : tags) {
            Row row = sheet.createRow(rowNum++);
            completeRow(row, tag);
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(xlsxReportFilePath);
        try (FileOutputStream fileOut = FileUtils.openOutputStream(file)) {
            workbook.write(fileOut);
            logger.info("Workbook is saved.");
        } catch (IOException e) {
            throw new ExcelReportException("Error of saving xslx file to system." + e.getMessage(), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("Error of closing workbook " + xlsxReportFilePath + ".");
                }
            }
        }
    }

    private void completeRow(Row row, Tag tag) {
        row.createCell(0)
                .setCellValue(tag.getPageUrl());

        row.createCell(1)
                .setCellValue(tag.getShortXPath());

        row.createCell(2)
                .setCellValue(tag.getFullXPath());

        row.createCell(3)
                .setCellValue(tag.getFullTagXPath());

        row.createCell(4)
                .setCellValue(tag.getName());
        setCellLongValue(row, 5, tag.getTextContent());
    }
}
