package com.productiveedge.content_mgmt_automation.entity.tag;

public interface Tag {
    void setParent(Tag parent);

    String getTextContent();

    String getName();

    //String[] getAttributes();

    String getHtmlContent();

    String getPageUrl();

    String getXPath();

    Tag getParent();
}
