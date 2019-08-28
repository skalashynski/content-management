package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.nio.file.Paths;
import java.util.Map;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.FlowHelper.generateDateFolderName;

@Data
public class GenerateExcelReportRequest extends Request {

    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH, XLSX_REPORT_NAME, REPORT_SHEET_NAME
    }

    private String reportPath;
    private String xlsxReportName;
    private String reportSheetName;

    public GenerateExcelReportRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.xlsxReportName = request.get(REQUEST_PARAMETER.XLSX_REPORT_NAME.name());
        this.reportSheetName =request.get(REQUEST_PARAMETER.REPORT_SHEET_NAME.name());
        this.reportPath = Paths.get(request.get(REQUEST_PARAMETER.ROOT_FOLDER_PATH.name()), FolderName.REPORT.name(), generateDateFolderName(), xlsxReportName).toString();
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (REQUEST_PARAMETER parameter : REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request to generate report.");
            }
        }
    }
}
