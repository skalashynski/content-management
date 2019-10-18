package com.productiveedge.content_mgmt_automation.entity.tag;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * class responsible for compose tags per page which have wrap each other and have the same textContent
 * Example:
 * <div>
 *     <div>
 *         <P>
 *             <a href ='something">Click here</a>
 *         </P>
 *     </div>
 * </div>
 *
 * theSameTextContentTags will compose 4 elements
 * reportTag will be ia 'a' element
 */
public class CompoundTag {
    protected List<Tag> theSameTextContentTags = new ArrayList<>();
    private Tag reportTag;

    public CompoundTag(List<Tag> theSameTextContentTags) {
        this.theSameTextContentTags = theSameTextContentTags;
    }

    public CompoundTag(Tag... tags) {
        super();
        add(tags);
    }

    public CompoundTag(Tag tag) {
        add(tag);
    }

    public CompoundTag(Tag reportTag, List<Tag> theSameTextContentTags) {
        this.reportTag = reportTag;
        this.theSameTextContentTags = theSameTextContentTags;
    }

    public void add(Tag... tags) {
        for (Tag tag : tags) {
            if (reportTag == null) {
                reportTag = tag;
            } else {
                if (reportTag.getFullTagXPath().compareTo(tag.getFullTagXPath()) > 0) {
                    reportTag = tag;
                }
            }
        }
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

    public String getReportTagXpath() {
        return this.reportTag.getFullTagXPath();
    }

    public List<Tag> getTheSameTextContentTags() {
        return theSameTextContentTags;
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
        return reportTag.getTextContent();
    }

    public String getPageUrl() {
        return reportTag.getPageUrl();
    }


}
