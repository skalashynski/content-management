package com.productiveedge.content_mgmt_automation.repository;

import java.util.Collection;

public interface Repositoty<T> {
    void saveAll(Collection<T> elements);
}
