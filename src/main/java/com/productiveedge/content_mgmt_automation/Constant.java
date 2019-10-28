package com.productiveedge.content_mgmt_automation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String TIME_PATTERN = "HH-mm";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH-mm-ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final LocalDateTime now = LocalDateTime.now();
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final String TIME = TIME_FORMATTER.format(now);
    private static final String DATE = DATE_FORMATTER.format(now);
    private static final String DATE_TIME = DATE_TIME_FORMATTER.format(now);

    public static String generateDate() {
        return DATE;
    }

    public static String generateDateTime() {
        return DATE_TIME;
    }

    public static String generateTime() {
        return TIME;
    }
}
