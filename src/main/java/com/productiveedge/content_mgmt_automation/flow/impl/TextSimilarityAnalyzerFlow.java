package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.request.TextSimilarityAnalyzerRequest;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Constant;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.flow.util.TagSimilarityAnalyzerFlowUtil;
import com.productiveedge.content_mgmt_automation.report.Report;
import com.productiveedge.content_mgmt_automation.report.exception.ReportException;
import com.productiveedge.content_mgmt_automation.report.impl.json.TestSimilarityJsonReport;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.Constant.generateDate;
import static com.productiveedge.content_mgmt_automation.Constant.generateDateTime;

public class TextSimilarityAnalyzerFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TextSimilarityAnalyzerFlow.class);

    private static final String[] BAD_TAGS = {Constant.IFRAME_TAG_NAME, Constant.NOSCRIPT_TAG_NAME, Constant.ROOT_TAG_NAME};


    private static final String TAG_REPORT_NAME = "text_similarity_tags_report";

    private final Report report;
    private final PageContainer pageContainer;
    //private final TagContainer2 tagContainer;
    private final TextContainer textContainer;
    private final TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest;
    private final String filePath;

    private final Predicate<BaseTag> badTagsFilter = tag -> !tag.getTextContent().isEmpty() && Arrays.stream(BAD_TAGS).noneMatch(e -> e.equalsIgnoreCase(tag.getName()));

    public TextSimilarityAnalyzerFlow(TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest) {
        //this.tagContainer = TagContainer2.getInstance();
        this.pageContainer = PageContainer.getInstance();
        this.textContainer = TextContainer.getInstance();
        this.textSimilarityAnalyzerRequest = textSimilarityAnalyzerRequest;
        this.filePath = getXlsxFilePath(TAG_REPORT_NAME + " " + generateDateTime());
        this.report = new TestSimilarityJsonReport(filePath);
    }

    private String getXlsxFilePath(String pageUrl) {
        return Paths.get(textSimilarityAnalyzerRequest.getDestinationFolder(), generateDate(), GrabAllLinksHelper.generateNameByKey(pageUrl)) + ".json";
    }

    @Override
    public void run() {
        pageContainer.getProcessedPageEntries().forEach(e -> {
            Page page = e.getValue();
            Document doc = Jsoup.parse(page.getHtmlContent());
            List<Element> pageElements;
            //grabbing all dom-elements from page
            if (textSimilarityAnalyzerRequest.isAnalyzeAllTags()) {
                pageElements = doc.getAllElements();
            } else {
                //grabbing user's specified elements from page
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
            pageRequestTagWithNotEmptyContent.forEach(tag -> textContainer.putTag(tag.getTextContent(), tag));
            //tagContainer.addTags(pageRequestTagWithNotEmptyContent);
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
