package com.productiveedge.content_mgmt_automation.entity.tag;

import lombok.Data;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BaseTag implements Tag {
    protected String pageUrl;
    protected String shortXPath;
    protected String fullXPath;
    protected String fullTagXPath;
    protected String name;
    protected String textContent;
    protected String htmlContent;
    protected int insideLevel;

    public BaseTag() {
    }


    public BaseTag(String pageUrl, String shortXPath, String fullXPath, String fullTagXPath, String tagName, String textHtml, String htmlContent) {
        this.pageUrl = TagUtil.validateData(pageUrl);
        this.shortXPath = TagUtil.validateData(shortXPath);
        this.fullXPath = TagUtil.validateData(fullXPath);
        this.fullTagXPath = TagUtil.validateData(fullTagXPath);
        this.name = TagUtil.validateData(tagName);
        this.textContent = TagUtil.validateData(textHtml);
        this.htmlContent = TagUtil.validateData(htmlContent);
    }

    public BaseTag(String pageUrl, String shortXPath, String fullXPath, String fullTagXPath, Element domElement) {
        this(pageUrl, shortXPath, fullXPath, fullTagXPath, domElement.tagName(), domElement.text(), domElement.html());
    }

    public BaseTag(String pageUrl, Element domElement) {
        this.pageUrl = TagUtil.validateData(pageUrl);
        this.shortXPath = TagUtil.validateData(generateShortXpath(domElement));
        this.fullXPath = TagUtil.validateData(generateFullXpath(domElement));
        this.fullTagXPath = TagUtil.validateData(generateFullXpathBasedOnTags(domElement));
        this.name = TagUtil.validateData(domElement.tagName());
        this.textContent = TagUtil.validateData(domElement.text());
        this.htmlContent = TagUtil.validateData(domElement.html());
    }

    private String generateShortXpath(Element element) {
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


    class Similiarity {
        private Tag tag;
        private double persent;
    }

    /*
    public static class BaseTagGroupBy {
        private String shortXPath;
        private String fullXPath;
        private String fullTagXPath;

        public BaseTagGroupBy() {
            this.shortXPath = BaseTag.this.shortXPath;
            this.fullXPath = BaseTag.this.fullXPath;
            this.fullTagXPath = BaseTag.this.fullTagXPath;
        }

    }
    */
}
