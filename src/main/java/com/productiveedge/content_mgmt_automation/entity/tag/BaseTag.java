package com.productiveedge.content_mgmt_automation.entity.tag;

import lombok.Data;
import org.jsoup.nodes.Element;

import static com.productiveedge.content_mgmt_automation.entity.tag.TagUtil.*;

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
        this.pageUrl = validateData(pageUrl);
        this.shortXPath = validateData(generateShortXpath(domElement));
        this.fullXPath = validateData(generateFullXpath(domElement));
        this.fullTagXPath = validateData(generateFullXpathBasedOnTags(domElement));
        this.name = validateData(domElement.tagName());
        this.textContent = validateData(domElement.text());
        this.htmlContent = validateData(domElement.html());
    }
}
