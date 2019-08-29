package com.productiveedge.content_mgmt_automation.repository.impl;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.repository.ExcelRepository;
import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PageExcelRepositoryImpl implements ExcelRepository<Page> {
    private static final Logger logger = LoggerFactory.getLogger(PageExcelRepositoryImpl.class);

    private final Workbook workbook;
    private final Sheet sheet;
    private final String xlsxReportFilePath;

    public PageExcelRepositoryImpl(String xlsxReportFilePath, String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.xlsxReportFilePath = xlsxReportFilePath;
        sheet = workbook.createSheet(sheetName);
    }

    private List<String> getColumns() {
        return Arrays.stream(Page.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    }

    private void createColumnHeaders(List<String> columns) {
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


    public void saveAll(Collection<Page> pages) throws ExcelException {

        List<String> columns = getColumns();

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

        File file = new File(xlsxReportFilePath);
        try (FileOutputStream fileOut = FileUtils.openOutputStream(file)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new ExcelException("Error of saving xslx file to system." + e.getMessage(), e);
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
