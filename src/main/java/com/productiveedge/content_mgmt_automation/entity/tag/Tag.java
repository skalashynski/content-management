package com.productiveedge.content_mgmt_automation.entity.tag;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Tag extends Cloneable {

    String getShortXPath();

    String getTextContent();

    String getName();

    String getPageUrl();

    String getFullXPath();

    String getFullTagXPath();

    void setTextContent(String text);

    void setHtmlContent(String html);

    default Optional<String> validateData(String data) {
        if (data != null) {
            return Optional.of(data.replaceAll("[\"]", "'"));
        }
        return Optional.empty();
    }

    default String generateShortXpath(Element element) {
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
            }
        }
        return xpath;
    }

    default String generateFullXpath(Element element) {
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
                resultXpath = itElementXpath + resultXpath;
            }
            parent = parent.parent();
            element = parent;
            itr--;
        }
        return resultXpath;
    }


    default String generateFullXpathBasedOnTags(Element element) {
        String xpath = "";
        String itElementXpath = "";
        Element parent = element.parent();
        while (parent != null) {
            if (!parent.tagName().equalsIgnoreCase(Constant.ROOT_TAG_NAME)) {
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
