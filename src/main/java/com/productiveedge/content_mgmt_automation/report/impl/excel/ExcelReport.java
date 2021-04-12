package com.productiveedge.content_mgmt_automation.report.impl.excel;

import com.productiveedge.content_mgmt_automation.report.Report;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.List;

public abstract class ExcelReport<T> implements Report<T> {
    private static final String CELL_WARNING_MESSAGE = "The content is too long. The maximum length of cell contents (text) is 32,767 characters.";
    protected final Workbook workbook;
    protected final Sheet sheet;
    protected final String xlsxReportFilePath;
    protected final File file;

    public ExcelReport(String xlsxReportFilePath, String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.xlsxReportFilePath = xlsxReportFilePath;
        this.sheet = workbook.createSheet(sheetName);
        this.file = new File(xlsxReportFilePath);
    }

    public abstract List<String> getHeaderNames();

    protected void createColumnHeaders(List<String> columns) {
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

    protected void setCellLongValue(Row row, int columnNumber, String cellValue) {
        Cell cell = row.createCell(columnNumber);
        try {
            cell.setCellValue(cellValue);
        } catch (IllegalArgumentException e) {
            //logger.warn("Row: " + cell.getAddress().getRow() + " , column: " + cell.getAddress().getColumn() + " . " + CELL_WARNING_MESSAGE);
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
