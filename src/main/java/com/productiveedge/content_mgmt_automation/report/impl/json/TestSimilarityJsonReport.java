package com.productiveedge.content_mgmt_automation.report.impl.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.report.Report;
import com.productiveedge.content_mgmt_automation.report.exception.ReportException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class TestSimilarityJsonReport implements Report<Map<String, Set<Page>>> {
    private static final Logger logger = LoggerFactory.getLogger(TestSimilarityJsonReport.class);
    private final File reportFile;
    private final String reportFilePath;

    public TestSimilarityJsonReport(String filePath) {
        //need to be checked
        this.reportFilePath = filePath;
        this.reportFile = new File(this.reportFilePath);
    }


    @Override
    public void saveAll(Map<String, Set<Page>> elements) throws ReportException {
        JsonArray groups = new JsonArray();
        elements.forEach((textKey, pages) -> {
            JsonObject arrayElement = new JsonObject();
            arrayElement.addProperty("text", textKey);
            JsonArray pagesJson = new JsonArray();
            //for-each trough pages
            pages.forEach(page -> {
                JsonObject pageJson = new JsonObject();
                pageJson.addProperty("url", page.getUrl());
                JsonArray xpathTags = new JsonArray();
                Set<Page.PageArea> areas = page.getTextAreas().get(textKey);
                if (areas != null) {
                    areas.forEach(area -> {
                        xpathTags.add(area.getReportTagXpath());
                    });
                    pageJson.add("xpaths", xpathTags);
                }
                pagesJson.add(pageJson);
            });
            arrayElement.add("pages", pagesJson);
            groups.add(arrayElement);
        });

        try {
            logger.info("Saving {} records to json file " + reportFilePath, groups.size());
            FileUtils.write(this.reportFile, new Gson().toJson(groups), StandardCharsets.UTF_8);
            logger.info("The data successfully saved to file " + reportFilePath);
        } catch (IOException e) {
            throw new ReportException("Error of saving json report. ", e);
        }
    }
}
