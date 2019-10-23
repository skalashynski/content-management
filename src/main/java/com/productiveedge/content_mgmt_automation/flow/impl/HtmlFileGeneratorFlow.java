package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.Constant;
import com.productiveedge.content_mgmt_automation.entity.request.SaveHtmlRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.PageInfoCollectorHelper;
import com.productiveedge.content_mgmt_automation.report.FileWriter;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class HtmlFileGeneratorFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(HtmlFileGeneratorFlow.class);

    private final SaveHtmlRequest saveHtmlRequest;
    private final PageContainer pageContainer;

    public HtmlFileGeneratorFlow(SaveHtmlRequest saveHtmlRequest) {
        this.saveHtmlRequest = saveHtmlRequest;
        this.pageContainer = PageContainer.getInstance();
    }

    @Override
    public void run() {
        pageContainer.getProcessedPageEntries().forEach(e -> {
            String filePath = Paths.get(saveHtmlRequest.getDestinationFolder(), Constant.generateDate(), PageInfoCollectorHelper.generateNameByKey(e.getKey())).toString() + ".html";
            try {
                FileWriter.write(filePath, e.getValue().getHtmlContent());
            } catch (IOException ex) {
                logger.error("Can't save html file of processUrl " + e.getValue().getUrl() + "." + IOUtils.LINE_SEPARATOR + ex.getMessage());
            }
        });
    }
}
