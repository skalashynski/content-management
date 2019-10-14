package com.productiveedge.content_mgmt_automation.repository.container.impl;


import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.repository.container.Container;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper.generateKey;

@Data
public final class PageContainer implements Container<String, Page> {

    private static final SortedMap<String, Page> cache = new TreeMap<>();


    public static long processedCacheWebsitesCount() {
        return cache.entrySet().stream().filter(e -> e.getValue().isProcessed()).count();
    }

    public static long unprocessedCacheWebsitesCount() {
        return cache.entrySet().stream().filter(e -> !e.getValue().isProcessed()).count();
    }

    public static Set<Map.Entry> getAllUnprocessedWebsiteLinks() {
        return cache.entrySet().stream().filter(e -> !e.getValue().isProcessed()).collect(Collectors.toSet());
    }

    public static Set<Map.Entry<String, Page>> getProcessedPageEntries() {
        return cache.entrySet().stream().filter(e -> e.getValue().getStatus().equals(Page.Status.PROCESSED)).collect(Collectors.toSet());
    }

    public static boolean isUnprocessedPageExist() {
        return cache.entrySet().stream()
                .anyMatch(e -> !e.getValue().isProcessed());
    }

    public static String getUnprocessedWebsiteLinkFromCache() {
        for (Map.Entry<String, Page> pageEntry : cache.entrySet()) {
            if (!pageEntry.getValue().isProcessed()) {
                return pageEntry.getValue().getUrl();
            }
        }
        return null;
    }

    public static Map.Entry<String, Page> nextUnprocessedPageEntry() {
        for (Map.Entry<String, Page> pageEntry : cache.entrySet()) {
            if (!pageEntry.getValue().isProcessed()) {
                return pageEntry;
            }
        }
        return null;
    }

    public Page getValue(String url) {
        return cache.get(generateKey(url));
    }

    public static void putPage(String url, Page value) {
        cache.put(generateKey(url), value);
    }

    public Map<String, Page> getCache() {
        return cache;
    }

    public static boolean containsLink(String link) {
        return !Objects.isNull(cache.get(generateKey(link)));
    }
}
