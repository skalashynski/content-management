package com.productiveedge.content_mgmt_automation.entity.tag;

import lombok.Data;
import org.jsoup.nodes.Element;


@Data
public class BaseTag implements Tag {

    protected String pageUrl;
    protected String shortXPath;
    protected String fullXPath;
    protected String fullTagXPath;
    protected String name;
    protected String textContent;
    protected String htmlContent;

    public BaseTag() {
    }


    public BaseTag(String pageUrl, String shortXPath, String fullXPath, String fullTagXPath, String tagName, String textHtml, String htmlContent) {
        this.pageUrl = validateData(pageUrl).orElse("");
        this.shortXPath = validateData(shortXPath).orElse("");
        this.fullXPath = validateData(fullXPath).orElse("");
        this.fullTagXPath = validateData(fullTagXPath).orElse("");
        this.name = validateData(tagName).orElse("");
        this.textContent = validateData(textHtml).orElse("");
        this.htmlContent = validateData(htmlContent).orElse("");
    }

    public BaseTag(String pageUrl, Element domElement) {
        this.pageUrl = validateData(pageUrl).orElse("");
        this.shortXPath = validateData(generateShortXpath(domElement)).orElse("");
        this.fullXPath = validateData(generateFullXpath(domElement)).orElse("");
        this.fullTagXPath = validateData(generateFullXpathBasedOnTags(domElement)).orElse("");
        this.name = validateData(domElement.tagName()).orElse("");
        this.textContent = validateData(domElement.text()).orElse("");
        this.htmlContent = validateData(domElement.html()).orElse("");
    }
}
