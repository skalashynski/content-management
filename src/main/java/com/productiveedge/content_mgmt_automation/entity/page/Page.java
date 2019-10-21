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

    public void addTag(Tag tag) {
        Set<PageArea> pageArea = textAreas.get(tag.getTextContent());
        String tagXpath = tag.getFullTagXPath();
        if (pageArea != null) {
            Set<PageArea> areas = pageArea.stream().filter(e -> {
                String eTagXPath = e.getTagXPath();
                //need to think how what is correct condition
                return eTagXPath.startsWith(tagXpath) || tagXpath.startsWith(eTagXPath);
            }).collect(Collectors.toSet());
            areas.forEach(e -> e.add(tag));
        } else {
            this.textAreas.put(tag.getTextContent(), new HashSet<>(Arrays.asList(new PageArea(tag))));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(url, page.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    /**
     * class responsible for compose tags per page which have wrap each other and have the same textContent
     * Example:
     * <div>
     * <div>
     * <p>
     * <a href ='something">Click here</a>
     * </P>
     * </div>
     * </div>
     * <p>
     * theSameTextContentTags will compose 4 elements
     * reportTag will be ia 'a' element
     */

    //нужно заменить название класса на PageArea
    public static class PageArea {
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
                    // нужно подумать, как всё-таки тут сравнивать теги
                    if (this.reportTag.getFullTagXPath().compareTo(tag.getFullTagXPath()) > 0) {
                        this.reportTag = tag;
                    }
                }
            }
            theSameTextContentTags.addAll(Arrays.asList(tags));
        }

        public String getReportTagXpath() {
            return this.reportTag.getFullTagXPath();
        }

        public String getPageUrl() {
            return reportTag.getPageUrl();
        }

        public String getTagXPath() {
            return reportTag.getFullTagXPath();
        }
    }
}
