package com.productiveedge.content_mgmt_automation.repository.container.impl;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.repository.container.Container;

import java.util.*;

public final class TextContainer implements Container<String, Set<Page>> {

    /**
     * key - textContent
     * value - page, which have that textContent
     */
    private static final SortedMap<String, Set<Page>> cache = new TreeMap<>();
    private PageContainer pageContainer;


    private TextContainer() {
        pageContainer = PageContainer.getInstance();
    }

    public static TextContainer getInstance() {
        return TextContainer.SingletonHolder.instance;
    }

    public void putTag(String text, Tag tag) {
        String pageUrl = tag.getPageUrl();
        Set<Page> pages = cache.get(text);

        if (pages != null) {
            Optional<Page> optionalPage = pages.stream()
                    .filter(e -> e.getUrl().equalsIgnoreCase(tag.getPageUrl()))
                    .findFirst();
            if (optionalPage.isPresent()) {
                optionalPage.ifPresent(e -> e.addTag(tag));
            } else {
                Page page = pageContainer.getValue(pageUrl);
                page.getTextAreas().put(text, new HashSet<>(Arrays.asList(new Page.PageArea(tag))));
                pages.add(page);
            }
        } else {
            Page page = pageContainer.getValue(pageUrl);
            page.getTextAreas().put(text, new HashSet<>(Arrays.asList(new Page.PageArea(tag))));
            cache.put(text, new HashSet<>(Arrays.asList(page)));
        }
    }

    public Set<Page> getValue(String text) {
        return cache.get(text);
    }

    @Override
    public Map<String, Set<Page>> getCache() {
        return cache;
    }

    private static class SingletonHolder {
        private static TextContainer instance = new TextContainer();
    }
}
