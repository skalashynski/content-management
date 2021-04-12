package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.request.TextSimilarityAnalyzerRequest;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Constant;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.PageInfoCollectorHelper;
import com.productiveedge.content_mgmt_automation.report.Report;
import com.productiveedge.content_mgmt_automation.report.exception.ReportException;
import com.productiveedge.content_mgmt_automation.report.impl.json.TextUniquenessJsonReport;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import com.productiveedge.content_mgmt_automation.repository.container.impl.TextContainer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.Constant.generateDate;
import static com.productiveedge.content_mgmt_automation.Constant.generateDateTime;

public class TextUniquenessAnalyzerFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TextUniquenessAnalyzerFlow.class);

    private static final String[] BAD_TAGS = {Constant.IFRAME_TAG_NAME, Constant.NOSCRIPT_TAG_NAME, Constant.ROOT_TAG_NAME};


    private static final String TAG_REPORT_NAME = "text_similarity_tags_report";

    private final Report report;
    private final PageContainer pageContainer;
    private final TextContainer textContainer;
    private final TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest;
    private final String filePath;

    private final Predicate<BaseTag> badTagsFilter = tag -> !tag.getTextContent().isEmpty() && Arrays.stream(BAD_TAGS).noneMatch(e -> e.equalsIgnoreCase(tag.getName()));

    public TextUniquenessAnalyzerFlow(TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest) {
        this.pageContainer = PageContainer.getInstance();
        this.textContainer = TextContainer.getInstance();
        this.textSimilarityAnalyzerRequest = textSimilarityAnalyzerRequest;
        this.filePath = getXlsxFilePath(generateDateTime() + " " + TAG_REPORT_NAME);
        this.report = new TextUniquenessJsonReport(filePath);
    }

    private String getXlsxFilePath(String pageUrl) {
        return Paths.get(textSimilarityAnalyzerRequest.getDestinationFolder(), generateDate(), FolderName.JSON.name(), PageInfoCollectorHelper.generateNameByKey(pageUrl)) + ".json";
    }

    @Override
    public void run() {
        Set<String> requiredTags = textSimilarityAnalyzerRequest.getTagsToAnalyze();
        logger.info("Grabbing data to json report............");
        pageContainer.getProcessedPageEntries().forEach(e -> {
            Page page = e.getValue();
            Document doc = Jsoup.parse(page.getHtmlContent());
            List<Element> pageElements;
            //grabbing all dom-elements from page
            if (textSimilarityAnalyzerRequest.isAnalyzeAllTags()) {
                pageElements = doc.getAllElements();
            } else {
                //grabbing user's specified elements from page
                pageElements = requiredTags.stream()
                        .map(doc::getElementsByTag)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
            pageElements.stream()
                    .map(el -> new BaseTag(page.getUrl(), el))
                    .filter(badTagsFilter)
                    //.filter(TagSimilarityAnalyzerFlowUtil.distinctByKeys(Tag::getTextContent, Tag::getFullXPath, Tag::getShortXPath, Tag::getName))
                    .forEach(tag -> textContainer.putTag(tag.getTextContent(), tag));
        });
        try {
            logger.info("Data of tag-report " + filePath + " is grabbed. Saving data to report.......");
            //FileWriter.write("report2.json", new Gson().toJson(textContainer.getCache().toString()));
            report.saveAll(textContainer.getCache());
            logger.info("Data is saved. The tag-report" + filePath + " is created.");
        } catch (ReportException ex) {
            logger.error("The tag-report" + filePath + " isn't created. " + ex.getMessage());
        }
    }
}
