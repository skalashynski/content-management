package com.productiveedge.content_mgmt_automation.repository.container.impl;

import com.productiveedge.content_mgmt_automation.entity.tag.CompoundTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.flow.util.TagSimilarityAnalyzerFlowUtil;
import com.productiveedge.content_mgmt_automation.repository.container.Container;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class TagContainer implements Container<String, CompoundTag> {
    private static final SortedMap<String, CompoundTag> cache = new TreeMap<>();

    public void putTag(Tag tag) {
        CompoundTag theSameTextContentTags = cache.get(tag.getTextContent());
        if (theSameTextContentTags != null) {
            theSameTextContentTags.add(tag);
        } else {
            cache.put(tag.getTextContent(), new CompoundTag(tag));
        }
    }

    public CompoundTag getValue(String text) {
        return cache.get(text);
    }

    public void addTags(List<Tag> tags) {
        tags.forEach(this::putTag);
    }

    @Override
    public Map<String, CompoundTag> getCache() {
        return cache;
    }

    public Map<String, List<CompoundTag>> getSimilarityData() {
        return cache.entrySet().stream()//get tags wit the same textContent
                .map(e -> TagSimilarityAnalyzerFlowUtil.compactGroupBasedOnTextContent(e.getValue().getTheSameTextContentTags()))
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(CompoundTag::getCommonText));
    }

}
