package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.response.CreateFolderResponse;
import com.productiveedge.content_mgmt_automation.entity.request.CreateLocalFolderRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CreateLocalFoldersFlow implements Flow<CreateFolderResponse, CreateLocalFolderRequest> {

    private CreateLocalFolderRequest request;

    private static void createFolder(Path folderPath) {
        File folder = new File(folderPath.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public CreateFolderResponse run(CreateLocalFolderRequest request) throws InvalidJarRequestException {
        CreateFolderResponse response = new CreateFolderResponse();
        validateClientRequest(request);
        Arrays.stream(FolderName.values()).forEach(e -> createFolder(Paths.get(request.getRootFolderPath(), e.name())));
        return response;
    }

    @Override
    public void validateClientRequest(CreateLocalFolderRequest request) throws InvalidJarRequestException {
        for (CreateLocalFolderRequest.REQUEST_PARAMETER parameter : CreateLocalFolderRequest.REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toLowerCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request");
            }
        }
        request.setRootFolderPath(request.get(CreateLocalFolderRequest.REQUEST_PARAMETER.ROOT_FOLDER_PATH.name().toLowerCase()));
    }
}
