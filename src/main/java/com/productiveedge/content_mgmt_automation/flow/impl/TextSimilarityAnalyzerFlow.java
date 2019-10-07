package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.entity.request.TextSimilarityAnalyzerRequest;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.flow.Flow;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.FlowHelper.generateDateFolderName;

public class TextSimilarityAnalyzerFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TextSimilarityAnalyzerFlow.class);
    private TagReportExcelRepositoryImpl repository;
    private final TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest;
    private final String SHEET_NAME = "similarity";
    private Comparator<BaseTag> compByLength = Comparator.comparingInt((BaseTag aTag) -> aTag.getTextContent().length());

    public TextSimilarityAnalyzerFlow(TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest) {
        this.textSimilarityAnalyzerRequest = textSimilarityAnalyzerRequest;
    }

    private String getXlsxFilePath(String pageUrl) {
        return Paths.get(textSimilarityAnalyzerRequest.getDestinationFolder(), generateDateFolderName(), SHEET_NAME, GrabAllLinksHelper.generateNameByKey(pageUrl)) + ".xlsx";
    }

    @Override
    public void run() {
        List<Tag> requestTagsAllPages = new ArrayList<>();
        PageContainer.getProcessedPageEntries().forEach(e -> {
            String filePath = getXlsxFilePath(e.getKey());
            Page page = e.getValue();
            Document doc = Jsoup.parse(page.getHtmlContent());
            List<Tag> pageRequestTagWithNotEmptyContent = textSimilarityAnalyzerRequest.getTagsToAnalyze().stream()
                    .map(doc::getElementsByTag)
                    .flatMap(Collection::stream)
                    .map(el -> new BaseTag(page.getUrl(), generateShortXpath(doc, el), /*generateShortXpath(el), */generateFullXpath(el), el))
                    .filter(tag -> !tag.getTextContent().isEmpty())
                    .collect(Collectors.groupingBy(BaseTag::getTextContent))
                    .values()
                    .stream()
                    .map(list -> {
                        list.sort((a, b) -> b.getFullXPath().length() - a.getFullXPath().length());
                        return list.get(0);
                    })
                    //.sorted(Comparator.comparingInt((BaseTag aTag) -> aTag.getTextContent().length()))
                    //.map(tag -> new )
                    //.filter(distinctByKey(Tag::getTextContent))
                    //.sorted(compByLength)
                    .collect(Collectors.toList());
            requestTagsAllPages.addAll(pageRequestTagWithNotEmptyContent);
            repository = new TagReportExcelRepositoryImpl(filePath, SHEET_NAME);
            try {
                repository.saveAll(requestTagsAllPages);
                logger.info("The report" + filePath + " is created.");
            } catch (ExcelException ex) {
                logger.error("The report" + filePath + " isn't created. " + ex.getMessage());
            }
        });

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private String generateShortXpath(Document doc, Element element) {
        String idAttr = element.id();
        if (idAttr != null) {
            if (!idAttr.isEmpty()) {
                return "//*[@id=\"" + idAttr + "\"]";
            }
        }

        String xpath = "";
        String classAttr = element.className();
        if (classAttr != null) {
            if (!classAttr.isEmpty()) {
                xpath += "//" + element.tagName() + "[@class=\"" + classAttr + "\"]";
                Element parent = element.parent();
                for (int i = 0; i < parent.children().size(); i++) {
                    if (parent.children().get(i).equals(element)) {
                        xpath += "[" + (i + 1) + "]";
                    }
                }
                //xpath += "and contains(text(), '" + element.text() + "')]";
            }
        }
        return xpath;
    }

    private String generateFullXpath(Element element) {
        StringBuilder xpath = new StringBuilder(element.tagName());
        Element parent = element.parent();
        while (parent != null) {
            if (!parent.tagName().equalsIgnoreCase("#root")) {
                xpath.insert(0, parent.tagName() + "/");
                List<Element> theSameTagNameElements = element.children().stream()
                        .filter(e -> e.tagName().equalsIgnoreCase(element.tagName()))
                        .collect(Collectors.toList());
                if (theSameTagNameElements.size() > 1) {
                    for (int j = 0; j < theSameTagNameElements.size(); j++) {
                        if (theSameTagNameElements.get(j).equals(element)) {
                            xpath.append("[").append(j + 1).append("]");
                            break;
                        }
                    }
                }
            }
            parent = parent.parent();
        }
        return xpath.toString();
    }


}
