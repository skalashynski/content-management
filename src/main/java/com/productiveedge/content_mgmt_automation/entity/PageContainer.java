package com.productiveedge.content_mgmt_automation.entity;



import com.productiveedge.content_mgmt_automation.flow.impl.GrabAllLinksFlow;
import lombok.Data;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Data
public final class PageContainer {

    private static final SortedMap<String, Page> cache = new TreeMap<>();

    private PageContainer() {

    }

    public static long processedCacheWebsitesCount() {
        return cache.entrySet().stream().filter(e -> e.getValue().isProcessed()).count();
    }

    public static long unprocessedCacheWebsitesCount() {
        return cache.entrySet().stream().filter(e -> !e.getValue().isProcessed()).count();
    }

    public static Set<Map.Entry> getAllUnprocessedWebsiteLinks() {
        return cache.entrySet().stream().filter(e -> !e.getValue().isProcessed()).collect(Collectors.toSet());
    }

    public static Set<Map.Entry> getAllProcessedWebsiteLinks() {
        return cache.entrySet().stream().filter(e -> e.getValue().isProcessed()).collect(Collectors.toSet());
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

    public static Map.Entry<String, Page> getUnprocessedPageEntry() {
        for (Map.Entry<String, Page> pageEntry : cache.entrySet()) {
            if (!pageEntry.getValue().isProcessed()) {
                return pageEntry;
            }
        }
        return null;
    }

    public static Page getPage(String url) {
        return cache.get(url);
    }

    public static void put(String key, Page value) {
        cache.put(key, value);
    }

    public static Map<String, Page> getCache() {
        return cache;
    }

    public static boolean containsLink(String link) {
        return cache.entrySet()
                .stream()
                .anyMatch(e -> {
                            try {
                                return GrabAllLinksFlow.convertUrlToCacheKey(e.getValue().getUrl()).equals(link);
                            } catch (MalformedURLException ex) {
                                return false;
                            }
                        }
                );
    }
}
