package com.productiveedge.content_mgmt_automation.flow.impl.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FlowHelper {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public  static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static String generateDateFolderName() {
        return DATE_FORMATTER.format(LocalDate.now());
    }
}
