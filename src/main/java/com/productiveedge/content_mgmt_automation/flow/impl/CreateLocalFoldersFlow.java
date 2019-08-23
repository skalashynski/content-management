package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.request.CreateLocalFolderRequest;
import com.productiveedge.content_mgmt_automation.entity.response.CreateFolderResponse;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CreateLocalFoldersFlow implements Flow<CreateFolderResponse> {


    private CreateLocalFolderRequest request;

    public CreateLocalFoldersFlow(CreateLocalFolderRequest request) {
        this.request = request;
    }

    private static void createFolder(Path folderPath) {
        File folder = new File(folderPath.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public CreateFolderResponse run() {
        Arrays.stream(FolderName.values()).forEach(e -> createFolder(Paths.get(request.getRootFolderPath(), e.name())));
        return null;
    }
}
