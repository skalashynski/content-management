package com.productiveedge.content_mgmt_automation.entity.container.impl;

import com.productiveedge.content_mgmt_automation.entity.container.Container;
import com.productiveedge.content_mgmt_automation.entity.tag.CompoundTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
}
