package com.productiveedge.content_mgmt_automation.flow.impl.helper;

import java.net.MalformedURLException;
import java.net.URL;
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

    /**
     * this condition is used to avoid redirect pages fot URL which ends with '/'
     */


    public static String convertURLKey(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
    public static String getDomain(String url) throws MalformedURLException {
        String domain = new URL(url).getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }



    public static String cutOffUrlProtocol(String urlLink) throws MalformedURLException {
        URL url = new URL(urlLink);
        return getDomain(urlLink) + url.getPath();
    }

}
