package com.productiveedge.content_mgmt_automation.entity.tag;

public interface Tag extends Cloneable {

    String getShortXPath();

    void setParent(Tag parent);

    String getTextContent();

    String getName();

    String getHtmlContent();

    String getPageUrl();

    String getFullXPath();

    String getFullTagXPath();

    Tag getParent();
}
