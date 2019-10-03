package com.productiveedge.content_mgmt_automation.entity.tag;

import lombok.Data;

import java.util.Deque;
import java.util.LinkedList;

@Data
public class BaseTag implements Tag {
    protected String pageUrl;
    protected String xPath;
    protected String name;
    protected String textContent;
    protected String htmlContent;
    protected Tag parent;

    public BaseTag() {
    }

    public BaseTag(String pageUrl, String name, String textContent, String htmlContent, Tag parent) {
        this.pageUrl = pageUrl;
        this.name = name;
        this.textContent = textContent;
        this.htmlContent = htmlContent;
        this.parent = parent;
    }

    @Override
    public String getXPath() {
        if (xPath == null || xPath.isEmpty()) {
            Deque<Tag> parentsTree = new LinkedList<>();
            Tag it = parent;
            while (it != null) {
                parentsTree.add(it);
                it = parent.getParent();
            }
            StringBuilder sb = new StringBuilder();
            parentsTree.forEach(e -> sb.append("/" + e.getName()));
            xPath = sb.toString();
        }
        return xPath;
    }


    class Similiarity {
        private Tag tag;
        private double persent;
    }
}
