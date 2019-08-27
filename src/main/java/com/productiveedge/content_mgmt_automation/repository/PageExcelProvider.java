package com.productiveedge.content_mgmt_automation.repository;

import com.productiveedge.content_mgmt_automation.entity.Page;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PageExcelProvider implements Repositoty<Page>{
    private static final Logger logger = LoggerFactory.getLogger(PageExcelProvider.class);

    private List<String> getColumns() {
        return Arrays.stream(Page.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    }


    public void saveAll(Collection<Page> pages) /*throws ExcelException */{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datatypes in Java");

        // Create a Font for styling header cells
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
        List<String> columns = getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }


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

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("productiveedge.xlsx")) {
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            //throw new ExcelException("Error of saving xslx file to system.", e);
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
