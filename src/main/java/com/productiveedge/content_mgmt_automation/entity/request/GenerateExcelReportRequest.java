package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.PageInfoCollectorHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.file.Paths;
import java.util.Map;

import static com.productiveedge.content_mgmt_automation.Constant.generateDate;
import static com.productiveedge.content_mgmt_automation.Constant.generateDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenerateExcelReportRequest extends Request {

    public static final String REPORT_SHEET_NAME = "Info";

    public GenerateExcelReportRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.xlsxReportName = generateDateTime() + " " + PageInfoCollectorHelper.generateNameByKey(request.get(REQUEST_PARAMETER.PAGE_DOMAIN_URL.name())) + ".xlsx";
        //request.get(REQUEST_PARAMETER.XLSX_REPORT_NAME.name());

        this.reportSheetName = REPORT_SHEET_NAME;
        //request.get(REQUEST_PARAMETER.REPORT_SHEET_NAME.name());

        this.reportPath = Paths.get(request.get(REQUEST_PARAMETER.ROOT_FOLDER_PATH.name()), FolderName.REPORT.name(), generateDate(), FolderName.EXCEL.name(), xlsxReportName).toString();
    }

    private String reportPath;
    private String xlsxReportName;
    private String reportSheetName;

    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH, PAGE_DOMAIN_URL//XLSX_REPORT_NAME, REPORT_SHEET_NAME
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
