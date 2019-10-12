package com.productiveedge.content_mgmt_automation.entity.tag;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * class responsible for compose tags per page which have wrap each other and have the same textContent
 */
public class CompoundTag {
    protected List<Tag> theSameTextContentTags = new ArrayList<>();
    private String theLongestXPath;

    public CompoundTag(List<Tag> theSameTextContentTags) {
        this.theSameTextContentTags = theSameTextContentTags;
    }

    public CompoundTag(Tag... tags) {
        super();
        add(tags);
    }

    public CompoundTag(String key, List<Tag> theSameTextContentTags) {
        this.theLongestXPath = key;
        this.theSameTextContentTags = theSameTextContentTags;
    }

    public void add(Tag... tags) {
        theSameTextContentTags.addAll(Arrays.asList(tags));
    }

    public void add(List<Tag> tags) {
        theSameTextContentTags.addAll(tags);
    }

    public void remove(Tag child) {
        theSameTextContentTags.remove(child);
    }

    public void remove(Tag... components) {
        this.theSameTextContentTags.removeAll(Arrays.asList(components));
    }

    public void clear() {
        this.theSameTextContentTags.clear();
    }


    @Deprecated
    public List<Tag> getElementsByTag(String tagName) {
        List tags = new ArrayList();
        if (this.theSameTextContentTags.size() == 0) {
            return new ArrayList<>();
        }
        this.theSameTextContentTags.forEach(e -> {
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

    public String getKey() {
        return theLongestXPath;
    }

    public void setKey(String key) {
        this.theLongestXPath = key;
    }

    public List<Tag> getTheSameTextContentTags() {
        return theSameTextContentTags;
    }

    @Deprecated
    public List<Tag> getElementsByTags(Collection<String> requestTagNames) {
        return requestTagNames.stream().map(e -> getElementsByTag(e)).flatMap(tags -> tags.stream()).collect(Collectors.toList());
    }

    public List<String> getPageUrls() {
        return theSameTextContentTags.stream().map(Tag::getPageUrl).collect(Collectors.toList());
    }


    public List<String> getShortXpath() {
        return theSameTextContentTags.stream().map(Tag::getShortXPath).collect(Collectors.toList());
    }

    public List<String> getFullXPath() {
        return theSameTextContentTags.stream().map(Tag::getFullXPath).collect(Collectors.toList());
    }

    public List<String> getFullTagXPath() {
        return theSameTextContentTags.stream().map(Tag::getFullTagXPath).collect(Collectors.toList());
    }

    public List<String> getName() {
        return theSameTextContentTags.stream().map(Tag::getName).collect(Collectors.toList());
    }

    public String getCommonText() {
        if (theSameTextContentTags.size() != 0) {
            return theSameTextContentTags.get(0).getTextContent();
        }
        return "";
    }

    public String getPageUrl() {
        if (theSameTextContentTags.size() != 0) {
            return theSameTextContentTags.get(0).getPageUrl();
        }
        return "";
    }


}
