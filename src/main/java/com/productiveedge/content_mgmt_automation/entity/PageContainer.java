package com.productiveedge.content_mgmt_automation.entity;


import com.productiveedge.content_mgmt_automation.flow.exception.InvalidHrefException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import javafx.beans.binding.ObjectExpression;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper.generateKey;

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
        return cache.get(generateKey(url));
    }

    public static void putPage(String url, Page value) {
        cache.put(generateKey(url), value);
    }

    public static Map<String, Page> getCache() {
        return cache;
    }

 /*   public static boolean containsLink(String link) {
        return cache.entrySet()
                .stream()
                .anyMatch(e -> {
                            try {
                                return GrabAllLinksHelper.cutOffUrlProtocol(e.getValue().getUrl()).equals(link);
                            } catch (InvalidHrefException ex) {
                                return false;
                            }
                        }
                );
    }*/

    public static boolean containsLink(String link) {
        return !Objects.isNull(cache.get(generateKey(link)));
    }
}
