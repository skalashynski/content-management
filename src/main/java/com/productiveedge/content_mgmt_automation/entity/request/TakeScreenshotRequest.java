package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.util.Map;

@Data
public class TakeScreenshotRequest extends Request {

    public enum REQUEST_PARAMETER {
        PAGE_SCREEN_SPACE_VALUE, BROWSER_NAME, ROOT_FOLDER_PATH, DRIVER_PATH
    }

    private String rootFolderPath;
    //private String operationSystem;
    private String browserName;
    private String pageScrollValue;
    private String driverPath;
    //private String browserPath;


    public TakeScreenshotRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.browserName = request.get(REQUEST_PARAMETER.BROWSER_NAME.name());
        //this.browserPath = request.get(REQUEST_PARAMETER.BROWSER_PATH.name());
        this.rootFolderPath = request.get(REQUEST_PARAMETER.ROOT_FOLDER_PATH.name());
        this.pageScrollValue = request.get(REQUEST_PARAMETER.PAGE_SCREEN_SPACE_VALUE.name());
        this.driverPath = request.get(REQUEST_PARAMETER.DRIVER_PATH.name());
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (REQUEST_PARAMETER parameter : REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request to take screenshots.");
            }
        }
    }
}
