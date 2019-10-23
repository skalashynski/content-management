package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.request.CreateLocalFolderRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.productiveedge.content_mgmt_automation.entity.FolderName.*;

public class FoldersCreatorFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(CreateLocalFolderRequest.class);

    private static final FolderName[] FOLDERS = {HTML, SCREEN, TXT, REPORT};


    private final CreateLocalFolderRequest request;

    public FoldersCreatorFlow(CreateLocalFolderRequest request) {
        this.request = request;
    }

    private static void createFolder(Path folderPath) {
        File folder = new File(folderPath.toString());
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                logger.info("Folder " + folderPath + " is created.");
            }
        } else {
            logger.info("Folder " + folderPath + " already exist.");
        }
    }

    @Override
    public void run() {
        Stream.of(FOLDERS).forEach(e -> createFolder(Paths.get(request.getRootFolderPath(), e.name())));
    }
}
