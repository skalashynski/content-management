package com.productiveedge.content_mgmt_automation.report.impl.excel;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.report.exception.ExcelReportException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PageExcelReportImpl extends ExcelReport<List<Page>> {
    private static final Logger logger = LoggerFactory.getLogger(PageExcelReportImpl.class);


    public PageExcelReportImpl(String xlsxReportFilePath, String sheetName) {
        super(xlsxReportFilePath, sheetName);
    }

    @Override
    public List<String> getHeaderNames() {
        return Arrays.stream(Page.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    }

    public void createColumnHeaders(List<String> columns) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Create a Row
        Row headerRow = sheet.createRow(0);
        // Create cells
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

    }

    @Override
    public void saveAll(List<Page> pages) throws ExcelReportException {

        List<String> columns = getHeaderNames();

        createColumnHeaders(columns);
        // Create Other rows and cells with page data
        int rowNum = 1;
        for (Page page : pages) {
            Row row = sheet.createRow(rowNum++);
            completeRowByPageData(row, page);
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = FileUtils.openOutputStream(this.file)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new ExcelReportException("Error of saving " + xlsxReportFilePath + " file to system. " + e.getMessage(), e);
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

    private void completeRowByPageData(Row row, Page page) {
        row.createCell(0)
                .setCellValue(page.getUrl());

        row.createCell(1)
                .setCellValue(page.isProcessed());

        row.createCell(2)
                .setCellValue(page.getStatus().name());

        row.createCell(3)
                .setCellValue(page.getMessageDescription());
        row.createCell(4)
                .setCellValue(page.getEmailHrefs().toString());
        row.createCell(5)
                .setCellValue(page.getExternalHrefs().toString());
        row.createCell(6)
                .setCellValue(page.getInternalHrefs().toString());
        row.createCell(7)
                .setCellValue(page.getPdfHrefs().toString());
        row.createCell(8)
                .setCellValue(page.getPngHrefs().toString());
        row.createCell(9)
                .setCellValue(page.getParentURLs().toString());
    }
}
