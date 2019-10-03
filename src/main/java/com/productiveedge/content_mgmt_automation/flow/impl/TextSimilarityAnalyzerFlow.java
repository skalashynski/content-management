package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.entity.request.SaveHtmlRequest;
import com.productiveedge.content_mgmt_automation.entity.request.TextSimilarityAnalyzerRequest;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.CompoundTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;
import com.productiveedge.content_mgmt_automation.repository.impl.TagReportExcelRepositoryImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.FlowHelper.generateDateFolderName;

public class TextSimilarityAnalyzerFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TextSimilarityAnalyzerFlow.class);
    private TagReportExcelRepositoryImpl repository;
    private final TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest;
    private final String SHEET_NAME = "similarity";

    public TextSimilarityAnalyzerFlow(TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest) {
        this.textSimilarityAnalyzerRequest = textSimilarityAnalyzerRequest;
    }

    private String getXlsxFilePath (String pageUrl) {
        return Paths.get(textSimilarityAnalyzerRequest.getDestinationFolder(), generateDateFolderName(), SHEET_NAME, GrabAllLinksHelper.generateNameByKey(pageUrl)) + ".xlsx";
    }

    @Override
    public void run() throws InvalidJarRequestException {
        List<Tag> tags = new ArrayList<>();
        PageContainer.getProcessedPageEntries().forEach(e-> {
            String filePath = getXlsxFilePath(e.getKey());
            Page page = e.getValue();


            Document doc = Jsoup.parse(page.getHtmlContent());
            Tag domModel = generateDOM(page.getUrl(), doc, null);
            if (domModel instanceof CompoundTag) {
                CompoundTag tag = (CompoundTag) domModel;
                List<Tag> tagsDiv = tag.getElementsByTag("div");
                tags.addAll(tagsDiv);
            }
            page.setTag(domModel);
            repository = new TagReportExcelRepositoryImpl(filePath, SHEET_NAME);
            try {
                repository.saveAll(tags);
                logger.info("The report" + filePath + " is created.");
            } catch (ExcelException ex) {
                logger.error("The report" + filePath + " isn't created. " + ex.getMessage());
            }
        });

    }

    /**
     * this is recursion
     */
    private Tag generateDOM(final String pageUrl, Element element, Tag parentTag) {
        List<Tag> childrenTags = new ArrayList<>();
        if (element.children().isEmpty()) {
            return new BaseTag(pageUrl, element.tagName(), element.text(), element.toString(), parentTag);
        }

        CompoundTag parent = new CompoundTag(pageUrl, element.tagName(), element.text(), element.toString(), parentTag);

        element.children().forEach(e -> {
            Tag tag = generateDOM(pageUrl, e, parent);
            childrenTags.add(tag);
        });

        //tags.forEach(t -> t.setParent(parent));
        parent.addChildElements(childrenTags);
        return parent;
    }
}
