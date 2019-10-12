package com.productiveedge.content_mgmt_automation.entity.container;

import java.util.Map;

public interface Container<K, V> {
    Map<K, V> getCache();

    V getValue(K key);
}
