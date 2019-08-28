package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.nio.file.Paths;
import java.util.Map;

@Data
public class SaveTxtRequest extends Request{
    private String destinationFolder;

    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH
    }

    public SaveTxtRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.destinationFolder = Paths.get(request.get(REQUEST_PARAMETER.ROOT_FOLDER_PATH.name()), FolderName.TXT.name()).toString();
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (SaveHtmlRequest.REQUEST_PARAMETER parameter : SaveHtmlRequest.REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request to save txt.");
            }
        }
    }
}
