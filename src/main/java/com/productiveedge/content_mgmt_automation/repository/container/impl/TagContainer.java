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

    /**
     * key - textContent
     * value - tags, which have that textContent
     */
    private static final SortedMap<String, CompoundTag> cache = new TreeMap<>();


    private TagContainer() {

    }

    public static TagContainer getInstance() {
        return TagContainer.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static TagContainer instance = new TagContainer();
    }

    public void putTag(Tag tag) {
        CompoundTag theSameTextContentTags = cache.get(tag.getTextContent());
        if (theSameTextContentTags != null) {
            theSameTextContentTags.add(tag);
        } else {
            //тут нужно переделывать
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
                .map(entry -> {
                    List<Tag> theSameTextThroughtAllPages = entry.getValue().getTheSameTextContentTags();
                    return TagSimilarityAnalyzerFlowUtil.compactGroupBasedOnTextContent(theSameTextThroughtAllPages);
                })
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(CompoundTag::getCommonText));
    }

}
