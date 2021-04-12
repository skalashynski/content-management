package com.productiveedge.content_mgmt_automation.flow.impl;

import java.util.EnumSet;

public enum DriverType {
    REMOTE,
    CHROME,
    FIREFOX,
    OPERA,
    SAFARI,
    PHANTOMJS,
    HTMLUNIT,
    IE,
    EDGE;

    public static EnumSet<DriverType> local = EnumSet.of(CHROME, FIREFOX, OPERA, SAFARI, IE, EDGE, PHANTOMJS, HTMLUNIT);

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}