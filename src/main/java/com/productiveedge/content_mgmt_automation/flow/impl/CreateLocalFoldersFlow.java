package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.request.CreateLocalFolderRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CreateLocalFoldersFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(CreateLocalFolderRequest.class);

    private CreateLocalFolderRequest request;

    public CreateLocalFoldersFlow(CreateLocalFolderRequest request) {
        this.request = request;
    }

    private static void createFolder(Path folderPath) {
        File folder = new File(folderPath.toString());
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                logger.info("Folder " + folderPath + " is created.");
            }
        } else {
            System.out.println(folderPath);
            logger.info("Folder " + folderPath + " already exist.");
        }
    }

    @Override
    public void run() {
        Arrays.stream(FolderName.values()).forEach(e -> createFolder(Paths.get(request.getRootFolderPath(), e.name())));
    }
}
