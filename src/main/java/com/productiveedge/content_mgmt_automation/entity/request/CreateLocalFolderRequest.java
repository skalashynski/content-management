package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.util.Map;

@Data
public class CreateLocalFolderRequest extends Request {
    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH
    }

    private String rootFolderPath;

    public CreateLocalFolderRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.rootFolderPath = request.get(REQUEST_PARAMETER.ROOT_FOLDER_PATH.name().toLowerCase());
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (REQUEST_PARAMETER parameter : REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toLowerCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request to create local folder");
            }
        }
    }
}
