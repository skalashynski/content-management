package com.productiveedge.content_mgmt_automation.entity.page;

import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;


/**
 * methods hashCode and equals are customised in this class.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Page {
    private String url;
    private boolean isProcessed;
    private Status status;
    private String messageDescription;
    private Set<String> emailHrefs;
    private Set<String> externalHrefs;
    private Set<String> internalHrefs;
    private Set<String> pdfHrefs;
    private Set<String> pngHrefs;
    private Set<String> parentURLs;
    private String htmlContent;
    private String txtContent;
    private Map<String, Set<PageArea>> textAreas;

    public enum Status {
        REDIRECT_OR_INVALID_URL, PROCESSED, UNPROCESSED,
    }

    public Page(String url) {
        this.url = url;
        isProcessed = false;
        status = Status.UNPROCESSED;
        externalHrefs = new HashSet<>();
        emailHrefs = new HashSet<>();
        internalHrefs = new HashSet<>();
        pdfHrefs = new HashSet<>();
        pngHrefs = new HashSet<>();
        parentURLs = new HashSet<>();
        messageDescription = "";
        htmlContent = null;
        txtContent = null;
        textAreas = new TreeMap<>();
    }

    public static Set<PageArea> findAreaByTagXpath(Set<PageArea> areas, Tag tag) {
        return areas.stream()
                .filter(e -> e.isRelevantTag(tag))
                .collect(Collectors.toSet());
    }

    public void addTag(Tag tag) {
        putTagToArea(tag);
    }

    private void putTagToArea(Tag tag) {
        Optional<Set<PageArea>> theSameTextContentAreas = getAreasWithTheSameTextContents(tag.getTextContent());
        if (theSameTextContentAreas.isPresent()) {
            Set<PageArea> theSameXpathAreas = findAreaByTagXpath(theSameTextContentAreas.get(), tag);
            if (theSameXpathAreas.size() > 0) {
                theSameXpathAreas.forEach(e -> e.add(tag));
            } else {
                this.textAreas.get(tag.getTextContent()).add(new PageArea(tag));
            }
        } else {
            Set<PageArea> set = new TreeSet<>(Comparator.comparing(PageArea::getReportTagXpath));
            set.add(new PageArea(tag));
            this.textAreas.put(tag.getTextContent(), set);
        }
    }

    private Optional<Set<PageArea>> getAreasWithTheSameTextContents(String text) {
        return Optional.ofNullable(textAreas.get(text));
    }

    /**
     * The Customised method in order to be safely saved to Set
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(url, page.url);
    }

    /**
     * The Customised method in order to be safely saved to Set
     *
     * */

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }


    public static class PageArea implements Comparable<PageArea> {

        private static final Comparator<Tag> TAG_COMPARATOR_BY_FULL_TAG_XPATH = Comparator.comparing(Tag::getFullTagXPath);

        @Override
        public int compareTo(PageArea o) {
            return this.reportTag.getFullTagXPath().compareTo(o.reportTag.getFullTagXPath());
        }

        private List<Tag> theSameTextContentTags = new ArrayList<>();
        private Tag reportTag;

        public PageArea(Tag tag) {
            add(tag);
        }

        public PageArea(Tag reportTag, List<Tag> theSameTextContentTags) {
            this.reportTag = reportTag;
            this.theSameTextContentTags = theSameTextContentTags;
        }

        public void add(Tag... tags) {
            for (Tag tag : tags) {
                if (this.reportTag == null) {
                    this.reportTag = tag;
                } else {
                    if (TAG_COMPARATOR_BY_FULL_TAG_XPATH.compare(this.reportTag, tag) > 0) {
                        this.reportTag = tag;
                    }
                }
            }
            theSameTextContentTags.addAll(Arrays.asList(tags));
        }

        public boolean isRelevantTag(Tag tag) {
            return reportTag.getFullTagXPath().startsWith(tag.getFullTagXPath()) || tag.getFullTagXPath().startsWith(reportTag.getFullTagXPath());
        }


        public String getReportTagXpath() {
            return this.reportTag.getFullTagXPath();
        }

        public String getPageUrl() {
            return reportTag.getPageUrl();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PageArea pageArea = (PageArea) o;
            return Objects.equals(theSameTextContentTags, pageArea.theSameTextContentTags) &&
                    Objects.equals(reportTag, pageArea.reportTag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(theSameTextContentTags, reportTag);
        }

        @Override
        public String toString() {
            return "PageArea{" +
                    "theSameTextContentTags=" + theSameTextContentTags +
                    ", reportTag=" + reportTag +
                    '}';
        }
    }
}
