package com.productiveedge.content_mgmt_automation.report.impl.excel;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.report.exception.ReportException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Deprecated
public class TextSimilarityExcelReportImp extends ExcelReport<Map<String, List<Page.PageArea>>> {
    private static final Logger logger = LoggerFactory.getLogger(TextSimilarityExcelReportImp.class);
    private static final List<String> COLUMN_HEADER_NAMES;

    static {
        COLUMN_HEADER_NAMES = Arrays.asList("text", "pages", "tag_xpath");
    }

    public TextSimilarityExcelReportImp(String xlsxReportFilePath, String sheetName) {
        super(xlsxReportFilePath, sheetName);
    }

    @Override
    public void saveAll(Map<String, List<Page.PageArea>> elements) throws ReportException {
        List<String> columns = getHeaderNames();

        createColumnHeaders(columns);
        // Create Other rows and cells with page data
        int rowNum = 1;

        logger.info("Completing workbook rows by data.");
        for (Map.Entry<String, List<Page.PageArea>> theSameTextEntry : elements.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            completeRow(row, theSameTextEntry);
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.size(); i++) {
            this.sheet.autoSizeColumn(i);
        }

        saveFile();
    }

    private synchronized void saveFile() throws ReportException {
        try (FileOutputStream fileOut = FileUtils.openOutputStream(this.file)) {
            logger.info("Saving workbook " + xlsxReportFilePath + "......");
            workbook.write(fileOut);
            logger.info("Workbook " + xlsxReportFilePath + " is saved.");
        } catch (IOException e) {
            throw new ReportException("Error of saving " + xlsxReportFilePath + " file to system. " + e.getMessage(), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("Error of closing workbook " + xlsxReportFilePath + "." + e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public List<String> getHeaderNames() {
        return COLUMN_HEADER_NAMES;
    }

    private void completeRow(Row row, Map.Entry<String, List<Page.PageArea>> entry) {
        row.createCell(0)
                .setCellValue(entry.getKey());
        row.createCell(1)
                .setCellValue(entry.getValue().stream().map(Page.PageArea::getPageUrl).collect(Collectors.joining(IOUtils.LINE_SEPARATOR)));
        row.createCell(2)
                .setCellValue(entry.getValue().stream().map(Page.PageArea::getReportTagXpath).collect(Collectors.joining(IOUtils.LINE_SEPARATOR)));
    }
}
