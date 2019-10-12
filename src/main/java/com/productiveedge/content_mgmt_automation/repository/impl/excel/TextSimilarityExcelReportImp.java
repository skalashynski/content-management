package com.productiveedge.content_mgmt_automation.repository.impl.excel;

import com.productiveedge.content_mgmt_automation.entity.tag.CompoundTag;
import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TextSimilarityExcelReportImp extends ExcelReport<List<CompoundTag>> {
    private static final Logger logger = LoggerFactory.getLogger(TextSimilarityExcelReportImp.class);

    public TextSimilarityExcelReportImp(String xlsxReportFilePath, String sheetName) {
        super(xlsxReportFilePath, sheetName);
    }

    @Override
    public void saveAll(List<CompoundTag> elements) throws ExcelException {
        List<String> columns = getColumns();

        createColumnHeaders(columns);
        // Create Other rows and cells with page data
        int rowNum = 1;

        for (CompoundTag element : elements) {
            Row row = sheet.createRow(rowNum++);
            completeRow(row, element);
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

    @Override
    public List<String> getColumns() {
        return Arrays.asList("pageUrl", "ShortXpath", "FullXPath", "FullTagXPath", "names");
    }

    private void completeRow(Row row, CompoundTag tag) {
        row.createCell(0)
                .setCellValue(tag.getKey());

        row.createCell(1)
                .setCellValue(formatCellValue(tag.getShortXpath()));

        row.createCell(2)
                .setCellValue(formatCellValue(tag.getFullXPath()));

        row.createCell(3)
                .setCellValue(formatCellValue(tag.getFullTagXPath()));

        row.createCell(4)
                .setCellValue(formatCellValue(tag.getName()));
        setCellValue(row, 5, tag.getCommonText());
    }

    private String formatCellValue(List<String> values) {
        return String.join("\n", values);
    }
}
