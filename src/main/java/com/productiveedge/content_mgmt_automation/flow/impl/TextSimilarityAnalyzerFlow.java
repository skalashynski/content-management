package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.request.TextSimilarityAnalyzerRequest;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.flow.util.TagSimilarityAnalyzerFlowUtil;
import com.productiveedge.content_mgmt_automation.report.Report;
import com.productiveedge.content_mgmt_automation.report.exception.ExcelReportException;
import com.productiveedge.content_mgmt_automation.report.impl.excel.TextSimilarityExcelReportImp2;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import com.productiveedge.content_mgmt_automation.repository.container.impl.TagContainer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.FlowHelper.generateDateFolderName;

public class TextSimilarityAnalyzerFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TextSimilarityAnalyzerFlow.class);
    private static final String IFRAME_TAG_NAME = "iframe";
    private static final String NOSCRIPT_TAG_NAME = "noscript";
    private static final String ROOT_TAG_NAME = "#root";
    private static final String[] BAD_TAGS = {IFRAME_TAG_NAME, NOSCRIPT_TAG_NAME, ROOT_TAG_NAME};


    private static final String SHEET_NAME = "similarity";
    private static final String TAG_REPORT_NAME = "text_similarity_tags_report";

    private final Report report;
    private final PageContainer pageContainer;
    private final TagContainer tagContainer;
    private final TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest;
    private final String filePath;

    private final Predicate<BaseTag> badTagsFilter = tag -> !tag.getTextContent().isEmpty() &&
            !tag.getName().equalsIgnoreCase(IFRAME_TAG_NAME) &&
            !tag.getName().equalsIgnoreCase(NOSCRIPT_TAG_NAME) &&
            !tag.getName().equalsIgnoreCase(ROOT_TAG_NAME);

    public TextSimilarityAnalyzerFlow(TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest) {
        this.tagContainer = TagContainer.getInstance();
        this.pageContainer = PageContainer.getInstance();
        this.textSimilarityAnalyzerRequest = textSimilarityAnalyzerRequest;
        this.filePath = getXlsxFilePath(TAG_REPORT_NAME);
        this.report = new TextSimilarityExcelReportImp2(filePath, SHEET_NAME);
    }

    private String getXlsxFilePath(String pageUrl) {
        return Paths.get(textSimilarityAnalyzerRequest.getDestinationFolder(), generateDateFolderName(), SHEET_NAME, GrabAllLinksHelper.generateNameByKey(pageUrl)) + ".xlsx";
    }

    @Override
    public void run() {
        List<Tag> requestTagsAllPages = new ArrayList<>();
        pageContainer.getProcessedPageEntries().forEach(e -> {
            Page page = e.getValue();
            Document doc = Jsoup.parse(page.getHtmlContent());
            List<Element> pageElements;
            if (textSimilarityAnalyzerRequest.isAnalyzeAllTags()) {
                pageElements = doc.getAllElements();
            } else {
                pageElements = textSimilarityAnalyzerRequest.getTagsToAnalyze().stream()
                        .map(doc::getElementsByTag)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
            List<Tag> pageRequestTagWithNotEmptyContent = pageElements.stream()
                    .map(el -> new BaseTag(page.getUrl(), el))
                    .filter(badTagsFilter)
                    .filter(TagSimilarityAnalyzerFlowUtil.distinctByKeys(Tag::getTextContent, Tag::getFullXPath, Tag::getShortXPath, Tag::getName))
                    .collect(Collectors.toList());
            tagContainer.addTags(pageRequestTagWithNotEmptyContent);
            requestTagsAllPages.addAll(pageRequestTagWithNotEmptyContent);
        });
        tagContainer.addTags(requestTagsAllPages);
        try {
            logger.info("Data of tag-report " + filePath + " is grabbed. Saving data to report.......");
            report.saveAll(tagContainer.getSimilarityData());
            //new TagCsvRepository("data.csv").saveAll(requestTagsAllPages);
            logger.info("Data is saved. The tag-report" + filePath + " is created.");
        } catch (ExcelReportException ex) {
            logger.error("The tag-report" + filePath + " isn't created. " + ex.getMessage());
        }
    }
}
