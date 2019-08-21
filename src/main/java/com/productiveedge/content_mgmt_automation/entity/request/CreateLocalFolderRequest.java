package com.productiveedge.content_mgmt_automation.entity.request;

import lombok.Data;

@Data
public class CreateLocalFolderRequest extends Request {
    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH
    }

    private String rootFolderPath;
}
