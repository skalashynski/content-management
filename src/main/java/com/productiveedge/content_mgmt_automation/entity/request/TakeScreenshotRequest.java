package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class TakeScreenshotRequest extends Request {

    public enum REQUEST_PARAMETERS {
        BROWSER_NAME, BROWSER_PATH, ROOT_FOLDER_PATH,
    }

    private String rootFolderPath;
    private String operationSystem;
    private String browserName;
    private String browserPath;
    private Collection<String> links;


    public TakeScreenshotRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.browserName = request.get(REQUEST_PARAMETERS.BROWSER_NAME.name());
        this.browserPath = request.get(REQUEST_PARAMETERS.BROWSER_PATH.name());
        this.rootFolderPath = request.get(REQUEST_PARAMETERS.ROOT_FOLDER_PATH.name());
        this.links = links;
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (REQUEST_PARAMETERS parameter : REQUEST_PARAMETERS.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request to take screenshot");
            }
        }
    }
}
