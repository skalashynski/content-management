package com.productiveedge.content_mgmt_automation.repository.impl;

import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
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
import java.util.List;
import java.util.stream.Collectors;

public class TagExcelRepositoryImpl implements ExcelRepository<Tag> {
    private static final String CELL_WARNING_MESSAGE = "The content is too long. The maximum length of cell contents (text) is 32,767 characters.";

    private static final Logger logger = LoggerFactory.getLogger(TagExcelRepositoryImpl.class);

    private final Workbook workbook;
    private final Sheet sheet;
    private final String xlsxReportFilePath;

    public TagExcelRepositoryImpl(String xlsxReportFilePath, String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.xlsxReportFilePath = xlsxReportFilePath;
        sheet = workbook.createSheet(sheetName);
    }

    private List<String> getColumns() {
        return Arrays.stream(BaseTag.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

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

    @Override
    public void saveAll(List<Tag> tags) throws ExcelException {
        List<String> columns = getColumns();

        createColumnHeaders(columns);
        // Create Other rows and cells with page data
        int rowNum = 1;
        for (Tag tag : tags) {
            Row row = sheet.createRow(rowNum++);
            completeRowByPageData(row, tag);
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

    private void completeRowByPageData(Row row, Tag tag) {
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
        setCellValue(row, 5, tag.getTextContent());
        //setCellValue(row, 5, tag.getHtmlContent());
    }

    private void setCellValue(Row row, int columnNumber, String cellValue) {
        Cell cell = row.createCell(columnNumber);
        try {
            cell.setCellValue(cellValue);
        } catch (IllegalArgumentException e) {
            logger.warn("Row: " + cell.getAddress().getRow() + " , column: " + cell.getAddress().getColumn() + " . " + CELL_WARNING_MESSAGE);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setColor(IndexedColors.RED.getIndex());
            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            cell.setCellValue(CELL_WARNING_MESSAGE);
            cell.setCellStyle(headerCellStyle);
        }

    }

}
