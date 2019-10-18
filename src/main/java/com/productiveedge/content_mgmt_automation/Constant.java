package com.productiveedge.content_mgmt_automation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH-mm-ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static String generateDate() {
        return DATE_FORMATTER.format(LocalDate.now());
    }

    public static String generateDateTime() {
        return DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }
}
