package com.productiveedge.content_mgmt_automation.repository.container.impl;


import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.repository.container.Container;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper.generateKey;

@Data
public final class PageContainer implements Container<String, Page> {


    private PageContainer() {

    }

    public static PageContainer getInstance() {
        return SingletonHolder.instance;
    }

    public long processedCacheWebsitesCount() {
        return cache.entrySet().stream().filter(e -> e.getValue().isProcessed()).count();
    }

    private static final SortedMap<String, Page> cache = new TreeMap<>();

    public long unprocessedCacheWebsitesCount() {
        return cache.entrySet().stream().filter(e -> !e.getValue().isProcessed()).count();
    }

    public Set<Map.Entry> getAllUnprocessedWebsiteLinks() {
        return cache.entrySet().stream().filter(e -> !e.getValue().isProcessed()).collect(Collectors.toSet());
    }

    public Set<Map.Entry<String, Page>> getProcessedPageEntries() {
        return cache.entrySet().stream().filter(e -> e.getValue().getStatus().equals(Page.Status.PROCESSED)).collect(Collectors.toSet());
    }

    public boolean isUnprocessedPageExist() {
        return cache.entrySet().stream()
                .anyMatch(e -> !e.getValue().isProcessed());
    }

    public String getUnprocessedWebsiteLinkFromCache() {
        for (Map.Entry<String, Page> pageEntry : cache.entrySet()) {
            if (!pageEntry.getValue().isProcessed()) {
                return pageEntry.getValue().getUrl();
            }
        }
        return null;
    }

    public Map.Entry<String, Page> nextUnprocessedPageEntry() {
        for (Map.Entry<String, Page> pageEntry : cache.entrySet()) {
            if (!pageEntry.getValue().isProcessed()) {
                return pageEntry;
            }
        }
        return null;
    }

    public void putPage(String url, Page value) {
        cache.put(generateKey(url), value);
    }

    public Page getValue(String url) {
        return cache.get(generateKey(url));
    }

    public boolean containsLink(String link) {
        return !Objects.isNull(cache.get(generateKey(link)));
    }

    public Map<String, Page> getCache() {
        return cache;
    }

    private static class SingletonHolder {
        private static PageContainer instance = new PageContainer();
    }
}
