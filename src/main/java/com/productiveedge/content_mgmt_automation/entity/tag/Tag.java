package com.productiveedge.content_mgmt_automation.entity.tag;

public interface Tag extends Cloneable {

    String getShortXPath();

    String getTextContent();

    String getName();

    String getPageUrl();

    String getFullXPath();

    String getFullTagXPath();

    void setTextContent(String text);

    void setHtmlContent(String html);
}
