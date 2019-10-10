package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.entity.request.TextSimilarityAnalyzerRequest;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import com.productiveedge.content_mgmt_automation.repository.exception.ExcelException;
import com.productiveedge.content_mgmt_automation.repository.impl.TagCsvRepository;
import com.productiveedge.content_mgmt_automation.repository.impl.TagExcelRepositoryImpl;
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
    private static final String IFRAME_TAG_NAME = "iframe";
    private static final String NOSCRIPT_TAG_NAME = "noscript";
    private static final String ROOT_TAG_NAME = "#root";
    private TagExcelRepositoryImpl repository;
    private final TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest;
    private final String SHEET_NAME = "similarity";
    private Comparator<BaseTag> compByLength = Comparator.comparingInt((BaseTag aTag) -> aTag.getTextContent().length());
    private Predicate<BaseTag> badTagsFilter = tag -> !tag.getTextContent().isEmpty() &&
            !tag.getName().equalsIgnoreCase(IFRAME_TAG_NAME) &&
            !tag.getName().equalsIgnoreCase(NOSCRIPT_TAG_NAME) &&
            !tag.getName().equalsIgnoreCase(ROOT_TAG_NAME);

    public TextSimilarityAnalyzerFlow(TextSimilarityAnalyzerRequest textSimilarityAnalyzerRequest) {
        this.textSimilarityAnalyzerRequest = textSimilarityAnalyzerRequest;
    }

    private String getXlsxFilePath(String pageUrl) {
        return Paths.get(textSimilarityAnalyzerRequest.getDestinationFolder(), generateDateFolderName(), SHEET_NAME, GrabAllLinksHelper.generateNameByKey(pageUrl)) + ".xlsx";
    }

    public static List<Tag> compactGroupBasedOnTextContent(List<Tag> tags) {
        List<Tag> result = new ArrayList<>();

        return tags.stream().collect(Collectors.groupingBy(Tag::getTextContent))
                .values()
                .stream()
                .map(list -> {
                    if (list.size() > 1) {
                        List<Tag> sorted = list.stream().sorted(Comparator.comparingInt((Tag aTag) -> aTag.getFullTagXPath().length())).collect(Collectors.toList());
                        Tag minFullTanXpathLengthBaseTag = sorted.get(0);
                        Tag maxFullTanXpathLengthBaseTag = sorted.get(sorted.size() - 1);
                        return Arrays.asList(minFullTanXpathLengthBaseTag, maxFullTanXpathLengthBaseTag);
                    }
                    return list;
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public void run() {
        List<Tag> requestTagsAllPages = new ArrayList<>();
        PageContainer.getProcessedPageEntries().forEach(e -> {
            String filePath = getXlsxFilePath(e.getKey());
            Page page = e.getValue();
            Document doc = Jsoup.parse(page.getHtmlContent());
            List<Tag> pageRequestTagWithNotEmptyContent = Collections.EMPTY_LIST;
            if (textSimilarityAnalyzerRequest.isAnalyzeAllTags()) {
                pageRequestTagWithNotEmptyContent = doc.getAllElements().stream()
                        .map(el -> new BaseTag(page.getUrl(), generateShortXpath(doc, el), /*generateShortXpath(el), */generateFullXpath(el), generateFullXpathBasedOnTags(el), el))
                        .filter(badTagsFilter)
/*
                        .collect(Collectors.groupingBy(BaseTag::getTextContent))
                        .values()
                        .stream()
                        .flatMap(List::stream)
                        /*
                        .map(list -> {
                            list.sort((a, b) -> b.getFullXPath().length() - a.getFullXPath().length());
                            return list.get(0);
                        })
                        */
                        //.sorted(Comparator.comparingInt((BaseTag aTag) -> aTag.getTextContent().length()))
                        //.map(tag -> new )
                        .filter(distinctByKeys(Tag::getTextContent, Tag::getFullXPath, Tag::getShortXPath, Tag::getName))
                        //.sorted(compByLength)
                        .collect(Collectors.toList());
                //pageRequestTagWithNotEmptyContent = compactGroupBasedOnTextContent(pageRequestTagWithNotEmptyContent);
            } else {
                pageRequestTagWithNotEmptyContent = textSimilarityAnalyzerRequest.getTagsToAnalyze().stream()
                        .map(doc::getElementsByTag)
                        .flatMap(Collection::stream)
                        .map(el -> new BaseTag(page.getUrl(), generateShortXpath(doc, el), /*generateShortXpath(el), */generateFullXpath(el), generateFullXpathBasedOnTags(el), el))
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
            }

            requestTagsAllPages.addAll(pageRequestTagWithNotEmptyContent);
            repository = new TagExcelRepositoryImpl(filePath, SHEET_NAME);
            try {
                //repository.saveAll(requestTagsAllPages);
                new TagCsvRepository("data.csv").saveAll(requestTagsAllPages);
                logger.info("The report" + filePath + " is created.");
            } catch (ExcelException ex) {
                logger.error("The report" + filePath + " isn't created. " + ex.getMessage());
            }
        });

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
                xpath += "//" + element.tagName() + "[@class=\"" + classAttr + "\"";
                if (element.text().length() > 10) {
                    xpath += " and starts-with(text(), \'" + element.text().substring(0, 10) + "\')";
                } else {
                    xpath += " and contains(text(), \'" + element.text() + "\')";
                }
                xpath += "]";
                Element parent = element.parent();
                /*
                for (int i = 0; i < parent.children().size(); i++) {
                    if (parent.children().get(i).equals(element)) {
                        xpath += "[" + (i + 1) + "]";
                    }
                }

                 */

            }
        }
        return xpath;
    }

    private String generateFullXpath(Element element) {
        String textXpath = "";
        if (element.text().length() > 10) {
            textXpath = "starts-with(text(), '" + element.text().substring(0, 10) + "\')";
        } else {
            textXpath = "contains(text(), '" + element.text() + "\')";
        }
        String idAttr = element.id();
        if (idAttr != null) {
            if (!idAttr.isEmpty()) {
                return "//*[@id=\"" + idAttr + "\"]";
            }
        }


        String itElementXpath = "";
        String resultXpath = "";
        //StringBuilder xpath = new StringBuilder(element.tagName());
        Element parent = element.parent();
        int itr = 3;
        while (parent != null && itr != 0) {
            if (!parent.tagName().equalsIgnoreCase("#root")) {
                final String tagName = element.tagName();
                itElementXpath = "//" + tagName;
                String classAttr = element.className();
                //checking 'class' attribute value
                List<Element> theSameTagNameOnTheLayerElements = parent.children().stream()
                        .filter(e -> e.tagName().equalsIgnoreCase(tagName))
                        .collect(Collectors.toList());


                if (classAttr != null) {
                    if (!classAttr.isEmpty()) {
                        //don't add class attribute to 'html' tag name
                        if (!element.tagName().equalsIgnoreCase("html")) {
                            itElementXpath += "[contains(@class, \'" + classAttr + "\')]";
                            //finding elements
                            List<Element> tagNameAndClassName = theSameTagNameOnTheLayerElements.stream().filter(e -> e.className().equalsIgnoreCase(classAttr)).collect(Collectors.toList());
                            if (tagNameAndClassName.size() > 1) {
                                for (int j = 0; j < tagNameAndClassName.size(); j++) {
                                    if (tagNameAndClassName.get(j).equals(element)) {
                                        itElementXpath += "[" + (j + 1) + "]";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (theSameTagNameOnTheLayerElements.size() > 1) {
                        for (int j = 0; j < theSameTagNameOnTheLayerElements.size(); j++) {
                            if (theSameTagNameOnTheLayerElements.get(j).equals(element)) {
                                itElementXpath += "[" + (j + 1) + "]";
                                break;
                            }
                        }
                    }
                }
                //adding 'text' value to xpath if domElement doesn't have child elements
                String tagText = element.text().trim();
                if (element.children().size() == 0 && !tagText.isEmpty()) {
                    tagText = tagText.length() > 10 ? tagText.substring(0, 10) : tagText;
                    itElementXpath += "[starts-with(text(), \'" + tagText + "\')]";
                }



                /*else {
                    itElementXpath += "/" + tagName;
                }*/
                resultXpath = itElementXpath + resultXpath;
            }
            parent = parent.parent();
            element = parent;
            itr--;
            //incorrect piece of code
                /*if (tagName.equalsIgnoreCase("p")) {
                    xpath += "[starts-with(text(), '" + (element.text().length() > 10 ? element.text().substring(0, 10) : element.text()) + "')]";
                }*/


        }
        return resultXpath;
    }


    private String generateFullXpathBasedOnTags(Element element) {
        String xpath = "";
        String itElementXpath = "";
        //StringBuilder xpath = new StringBuilder(element.tagName());
        Element parent = element.parent();
        while (parent != null) {
            if (!parent.tagName().equalsIgnoreCase("#root")) {
                final String elementTagName = element.tagName();
                itElementXpath = "//" + elementTagName;
                List<Element> theSameTagNameElementsOnTheLayer = parent.children().stream()
                        .filter(e -> e.tagName().equalsIgnoreCase(elementTagName))
                        .collect(Collectors.toList());
                if (theSameTagNameElementsOnTheLayer.size() > 1) {
                    for (int j = 0; j < theSameTagNameElementsOnTheLayer.size(); j++) {
                        if (theSameTagNameElementsOnTheLayer.get(j).equals(element)) {
                            itElementXpath += "[" + (j + 1) + "]";
                            break;
                        }
                    }
                }
                xpath = itElementXpath + xpath;
            }
            element = parent;
            parent = parent.parent();

        }
        return xpath;
    }

}
