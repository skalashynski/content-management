package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.request.SaveHtmlRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.FlowHelper.generateDateFolderName;

public class SaveHtmlFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(SaveHtmlFlow.class);

    private final SaveHtmlRequest saveHtmlRequest;

    public SaveHtmlFlow(SaveHtmlRequest saveHtmlRequest) {
        this.saveHtmlRequest = saveHtmlRequest;
    }

    @Override
    public void run() {
        PageContainer.getProcessedPageEntries().forEach(e-> {
            String filePath = Paths.get(saveHtmlRequest.getDestinationFolder(), generateDateFolderName(),  GrabAllLinksHelper.generateNameByKey(e.getKey())).toString() + ".html";
            File destinationFile = new File(filePath);
            try {
                FileUtils.writeStringToFile(destinationFile, e.getValue().getHtmlContent(), "UTF8");
            } catch (IOException ex) {
                logger.error("Can't save html file of processUrl " + e.getValue().getUrl() + ".\n" + ex.getMessage());
            }
        });
    }
}
