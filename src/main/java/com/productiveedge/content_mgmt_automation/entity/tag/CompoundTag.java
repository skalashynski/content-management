package com.productiveedge.content_mgmt_automation.entity.tag;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompoundTag extends BaseTag {
    protected List<Tag> children = new ArrayList<>();

    public CompoundTag(List<Tag> children) {
        this.children = children;
    }


    public CompoundTag(String pageUrl, String name, String textContent, String htmlContent, Tag parentTag) {
        super(pageUrl, name, textContent, htmlContent, parentTag);
    }

    public CompoundTag(String pageUrl, String name, String textContent, String htmlContent, Tag parentTag, List<Tag> children) {
        super(pageUrl, name, textContent, htmlContent, parentTag);
        this.children = children;
    }

    public CompoundTag(Tag... tags) {
        super();
        addChildElements(tags);
    }

    public void addChildElements(Tag... tags) {
        children.addAll(Arrays.asList(tags));
    }

    public void addChildElements(List<Tag> tags) {
        children.addAll(tags);
    }

    public void remove(Tag child) {
        children.remove(child);
    }

    public void remove(Tag... components) {
        children.removeAll(Arrays.asList(components));
    }

    public void clear() {
        children.clear();
    }

    public List<Tag> getElementsByTag(String tagName) {
        List tags = new ArrayList();
        if (children.size() == 0) {
            return new ArrayList<>();
        }
        children.forEach(e -> {
            if (e instanceof CompoundTag) {
                CompoundTag tag = (CompoundTag) e;
                tags.addAll(tag.getElementsByTag(tagName));
            } else {
                if (e.getName().equalsIgnoreCase(tagName)) {
                    tags.add(e);
                }
            }
        });
        return tags;
    }

/*    @Override
    public String getHtmlContent() {
        StringBuilder b = new StringBuilder();
        if (children.size() == 0) {
            return this.textContent;
        }
        children.forEach(e -> {
            if (e instanceof CompoundTag) {
                CompoundTag tag = (CompoundTag) e;
                tags.addAll(tag.getElementsByTag(tagName));
            } else {
                if (e.getName().equalsIgnoreCase(tagName)) {
                    tags.addChildElements(e);
                }
            }
        });
        return super.getHtmlContent();
    }*/
}
