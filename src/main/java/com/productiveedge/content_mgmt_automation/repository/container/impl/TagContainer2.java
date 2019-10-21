package com.productiveedge.content_mgmt_automation.repository.container.impl;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.repository.container.Container;

import java.util.*;

public final class TagContainer2 implements Container<String, Map<String, Map<String, Page.PageArea>>> {

    /**
     * key - textContent
     * value - tags, which have that textContent trough all pages
     * <p>
     * text_value
     * pageUrl
     * text_value
     * List<PageArea>
     */
    private static final SortedMap<String, Map<String, Map<String, Page.PageArea>>> cache = new TreeMap<>();


    private TagContainer2() {

    }

    public static TagContainer2 getInstance() {
        return TagContainer2.SingletonHolder.instance;
    }

    public void putTag(Tag tag) {
        String tagText = tag.getTextContent();
        String pageUrl = tag.getPageUrl();
        Map<String, Map<String, Page.PageArea>> pages = cache.get(tagText);
        if (pages != null) {
            Map<String, Page.PageArea> page = pages.get(pageUrl);
            if (page != null) {
                Page.PageArea tagGroupPerText = page.get(tagText);
                if (tagGroupPerText != null) {
                    tagGroupPerText.add(tag);
                } else {
                    page.put(tagText, new Page.PageArea(tag));
                }
            } else {
                pages.put(pageUrl, new HashMap<String, Page.PageArea>() {{
                    put(tagText, new Page.PageArea(tag));
                }});
            }
        } else {
            cache.put(tagText, new HashMap<String, Map<String, Page.PageArea>>() {{
                put(pageUrl, new HashMap<String, Page.PageArea>() {{
                    put(tagText, new Page.PageArea(tag));
                }});
            }});
        }
    }

    public void addTags(List<Tag> tags) {
        tags.forEach(this::putTag);
    }

    @Override
    public Map<String, Map<String, Map<String, Page.PageArea>>> getCache() {
        return cache;
    }

    @Override
    public Map<String, Map<String, Page.PageArea>> getValue(String key) {
        return cache.get(key);
    }

    private static class SingletonHolder {
        private static TagContainer2 instance = new TagContainer2();
    }
}
