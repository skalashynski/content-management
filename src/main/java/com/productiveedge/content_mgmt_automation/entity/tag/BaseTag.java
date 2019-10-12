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
    protected Tag parent;
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

    public String getFullXPath() {
        if (fullXPath == null || fullXPath.isEmpty()) {
            if (parent != null) {
                String parentXpath = parent.getFullXPath();
                if (parentXpath != null) {
                    if (!parentXpath.isEmpty()) {
                        fullXPath = parentXpath + "/" + name;
                    }
                } else {
                    fullXPath = name;
                }

            }
        }
        return fullXPath;
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
