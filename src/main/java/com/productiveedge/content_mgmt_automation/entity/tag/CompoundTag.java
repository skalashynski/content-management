package com.productiveedge.content_mgmt_automation.entity.tag;


import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CompoundTag extends BaseTag {
    protected List<Tag> childrenTags = new ArrayList<>();

    public CompoundTag(List<Tag> childrenTags) {
        this.childrenTags = childrenTags;
    }

    public CompoundTag(String pageUrl, String shortXPath, String xPath, String fullTagXPath, Element domElement) {
        super(pageUrl, shortXPath, xPath, fullTagXPath, domElement);
    }

    public CompoundTag(String pageUrl, String shortXPath, String xPath, String fullTagXPath, Element domElement, List<Tag> childrenTags) {
        super(pageUrl, shortXPath, xPath, fullTagXPath, domElement);
        this.childrenTags = childrenTags;
    }

    public CompoundTag(Tag... tags) {
        super();
        addChildElements(tags);
    }

    public void addChildElements(Tag... tags) {
        childrenTags.addAll(Arrays.asList(tags));
    }

    public void addChildElements(List<Tag> tags) {
        childrenTags.addAll(tags);
    }

    public void remove(Tag child) {
        childrenTags.remove(child);
    }

    public void remove(Tag... components) {
        this.childrenTags.removeAll(Arrays.asList(components));
    }

    public void clear() {
        this.childrenTags.clear();
    }

    public List<Tag> getElementsByTag(String tagName) {
        List tags = new ArrayList();
        if (this.childrenTags.size() == 0) {
            return new ArrayList<>();
        }
        this.childrenTags.forEach(e -> {
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

    public List<Tag> getElementsByTags(Collection<String> requestTagNames) {
        return requestTagNames.stream().map(e -> getElementsByTag(e)).flatMap(tags -> tags.stream()).collect(Collectors.toList());
    }
}
