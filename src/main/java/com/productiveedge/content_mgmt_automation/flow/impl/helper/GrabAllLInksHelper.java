package com.productiveedge.content_mgmt_automation.flow.impl.helper;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GrabAllLInksHelper {
    private static final String A_TAG_REGEX = "<a[^>]* href=\"([^\"]*)\"";
    private static final Pattern A_TAG_PATTERN = Pattern.compile(A_TAG_REGEX);

    public static Set<String> extractHtmlHrefs(String html) {
        Set<String> result = new HashSet<>();
        Matcher matcher = A_TAG_PATTERN.matcher(html);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result
                .parallelStream()
                .map(e -> e.substring(e.indexOf("href=\""), e.length() - 1).substring(6))
                .collect(Collectors.toSet());
    }
}
