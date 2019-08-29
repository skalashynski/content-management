package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.entity.PageBuilder;
import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidHrefException;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.exception.ProcessPageException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import com.productiveedge.content_mgmt_automation.service.ApacheHttpClient;
import com.productiveedge.content_mgmt_automation.service.exception.ApacheHttpClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.productiveedge.content_mgmt_automation.entity.Page.Status.PROCESSED;
import static com.productiveedge.content_mgmt_automation.entity.Page.Status.REDIRECT_OR_INVALID_URL;
import static com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper.generateKey;

public class GrabAllLinksFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(GrabAllLinksFlow.class);

    private static final String ANCHOR_SYMBOL = "#";
    private static final String QUESTION_SYMBOL = "?";
    private static final String JAVASCRIPT_HREF = "javascript";
    private static final String MAIL_HREF = "mailto";
    private static final String PHONE_HREF = "tel";
    private static final String PDF_HREF = ".pdf";
    private static final String PNG_HREF = ".png";
    private static final String GIF_HREF = ".gif";
    private static final String JPEG_HREF = ".jpeg";
    private static final String ZIP_HREF = ".zip";


    private static final Predicate<String> isAnchorHref = href -> href.equals(ANCHOR_SYMBOL);
    private static final Predicate<String> isJavascriptHref = href -> href.startsWith(JAVASCRIPT_HREF);
    private static final Predicate<String> isPhoneNumberHref = href -> href.startsWith(PHONE_HREF);
    private static final Predicate<String> isMailHref = href -> href.startsWith(MAIL_HREF);
    private static final Predicate<String> isPdfHref = href -> href.contains(PDF_HREF);

    private final Predicate<String> isPictureHref = href -> href.endsWith(PNG_HREF) || href.endsWith(GIF_HREF) || href.endsWith(JPEG_HREF) || href.endsWith(ZIP_HREF);

    private final GrabAllLinksRequest request;

    public GrabAllLinksFlow(GrabAllLinksRequest request) {
        this.request = request;
    }


    private static boolean isRubbishHref(String href) {
        return isPhoneNumberHref.or(isAnchorHref).or(isJavascriptHref).test(href);
    }

    @SafeVarargs
    private static <T> Set<T> combine(Set<T>... sets) {
        return Stream.of(sets).flatMap(Set::stream).collect(Collectors.toSet());
    }


    private static String convertDaughterHref(String grabbedDaughterHref, String parentHref) throws InvalidHrefException {
        grabbedDaughterHref = grabbedDaughterHref.trim().toLowerCase();
        if (isRubbishHref(grabbedDaughterHref)) {
            grabbedDaughterHref = parentHref;
        }
        try {
            URL mergedURL = new URL(new URL(parentHref), grabbedDaughterHref);
            return mergedURL.toString();
        } catch (MalformedURLException e) {
            throw new InvalidHrefException(e);
        }
    }

    private static URL concatURLs(String absoluteUrl, String relativeUrl) throws InvalidHrefException {
        try {
            return new URL(new URL(absoluteUrl), relativeUrl);
        } catch (MalformedURLException e) {
            throw new InvalidHrefException("Invalid concatenation of " + absoluteUrl + " + " + relativeUrl, e);
        }
    }

    private static String cutOffURLParameters(String relativeUrl) throws InvalidHrefException {
        try {
            if (relativeUrl.contains(QUESTION_SYMBOL)) {
                relativeUrl = new URL(relativeUrl.substring(0, relativeUrl.indexOf(QUESTION_SYMBOL))).toString();
            }
            if (relativeUrl.contains(ANCHOR_SYMBOL)) {
                relativeUrl = new URL(relativeUrl.substring(0, relativeUrl.indexOf(ANCHOR_SYMBOL))).toString();
            }
            if (relativeUrl.endsWith(ANCHOR_SYMBOL)) {
                relativeUrl = new URL(relativeUrl.substring(0, relativeUrl.length() - 1)).toString();
            }
            return relativeUrl;
        } catch (MalformedURLException e) {
            throw new InvalidHrefException("Invalid cutting off url parameters: " + relativeUrl, e);
        }
    }

    private Set<String> filterInternalHrefs(Set<String> daughterHrefs, String parentUrl) throws ProcessPageException {
        try {
            final String parentDomain = GrabAllLinksHelper.getDomain(parentUrl);
            return daughterHrefs
                    .stream()
                    .filter(it -> {
                                try {
                                    String daughterDomain = GrabAllLinksHelper.getDomain(it);
                                    return parentDomain.equals(daughterDomain);
                                } catch (InvalidHrefException e) {
                                    logger.warn("Can't extract domain from url " + it + ".\n" + e.getMessage());
                                    return false;
                                }
                            }
                    ).collect(Collectors.toSet());
        } catch (InvalidHrefException e) {
            throw new ProcessPageException(e);
        }
    }


    private Set<String> findDifferenceHrefs(Set<String> hrefs, Set<String> internalHrefs) {
        Set<String> difference = new HashSet<>(hrefs);
        difference.removeAll(internalHrefs);
        return difference;
    }


    private Set<String> filterHrefs(Set<String> hrefs, Predicate<String> predicate) {
        return hrefs
                .stream()
                .filter(predicate)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    //нужно пофиксать
    private static boolean isAbsoluteHref(String href) {
        try {
            new URL(href);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private static final BiFunction<String, String, String> convertInternalHref = (internalHref, pageUrl) -> {
        try {
            if (!isAbsoluteHref(internalHref)) {
                internalHref = concatURLs(pageUrl, internalHref).toString();
            }
            return cutOffURLParameters(internalHref);
        } catch (InvalidHrefException ex) {
            //log
            System.out.println(ex.getMessage());
            return null;
        }
    };


    private static Set<String> convertInternalHrefs(Set<String> internalHrefSet, String pageUrl) {
        return internalHrefSet
                .stream()
                .map(e -> convertInternalHref.apply(e, pageUrl))
                .filter(e -> !Objects.isNull(e))
                .collect(Collectors.toSet());
    }

    private void initPageContainer() throws InvalidJarRequestException {
        String startPageUrl;
        startPageUrl = request.getProcessUrl();
        String key = generateKey(startPageUrl);
        Page page = new Page(startPageUrl);
        PageContainer.putPage(key, page);
    }

    private Page processPage(Page page) throws ProcessPageException {
        logger.info("Processing page " + page.getUrl());
        String webPageUrl = page.getUrl();
        try {
            String htmlContent = getHtmlPageContent(webPageUrl);
            Set<String> allValidHrefSet = extractHrefsFromWebsite(htmlContent, webPageUrl);
            Set<String> emailHrefSet = filterHrefs(allValidHrefSet, isMailHref);
            Set<String> pdfHrefSet = filterHrefs(allValidHrefSet, isPdfHref);
            Set<String> pngHrefSet = filterHrefs(allValidHrefSet, isPictureHref);
            Set<String> internalAndExternalHrefSet = findDifferenceHrefs(allValidHrefSet, combine(emailHrefSet, pdfHrefSet, pngHrefSet));
            Set<String> internalHrefSet = filterInternalHrefs(internalAndExternalHrefSet, webPageUrl);
            Set<String> externalHrefSet = findDifferenceHrefs(internalAndExternalHrefSet, internalHrefSet);

            //cut off parameters of internal hrefs
            // & add domain name if the internalHref starts with '\'
            internalHrefSet = convertInternalHrefs(internalHrefSet, webPageUrl);

            addParentUrlToCachePages(internalHrefSet, webPageUrl);
            putURLsToCache(internalHrefSet, webPageUrl);

            page = new PageBuilder(page).setUrl(webPageUrl)
                    .setProcessed(true)
                    .setStatus(PROCESSED)
                    .setEmailHrefs(emailHrefSet)
                    .setExternalHrefs(externalHrefSet)
                    .setInternalHrefs(internalHrefSet)
                    .setPdfHrefs(pdfHrefSet)
                    .setPngHrefs(pngHrefSet)
                    .setHtmlContent(htmlContent)
                    .build();
            logger.info("Page " + page.getUrl() + "is processed successfully");
            return page;
        } catch (ApacheHttpClientException e) {
            throw new ProcessPageException("Can't execute http-get request to url " + webPageUrl + ".\n" + e.getMessage(), e);
        }

    }


    @Override
    public void run() throws InvalidJarRequestException {
        initPageContainer();
        int processCount = request.getProcessUrlCount();
        Page page;

        while (PageContainer.isUnprocessedPageExist() && PageContainer.processedCacheWebsitesCount() < processCount) {
            Map.Entry<String, Page> unprocessedPageEntry = PageContainer.nextUnprocessedPageEntry();
            String cacheKey = unprocessedPageEntry.getKey();
            page = unprocessedPageEntry.getValue();
            try {
                page = processPage(page);
            } catch (ProcessPageException e) {
                logger.error("Page " + page.getUrl() + " is unprocessed. " + e.getMessage());
                page = new Page(page.getUrl());
                page.setProcessed(true);
                page.setStatus(REDIRECT_OR_INVALID_URL);
                page.setMessageDescription(e.getMessage());
            } finally {
                PageContainer.putPage(cacheKey, page);
            }
        }
    }

    //нужно пофиксать
    //bad piece of code
    private Map<String, String> generateHttpHeaders() {
        return new HashMap<String, String>() {{
            put("allow_redirect", request.getAllowRedirect());
        }};

    }

    private String getHtmlPageContent(String url) throws ApacheHttpClientException {
        return ApacheHttpClient.sendGet(url, generateHttpHeaders());
    }

    private Set<String> extractHrefsFromWebsite(String htmlContent, String pageUrl) {
        Set<String> hrefs = GrabAllLinksHelper.extractHtmlHrefs(htmlContent);
        return hrefs
                .stream()
                .map(daughterHref -> {
                            try {
                                return convertDaughterHref(daughterHref, pageUrl);
                            } catch (InvalidHrefException e) {
                                logger.warn("Can't convert href " + daughterHref + " located on " + pageUrl + ".\n" + e.getMessage());
                                return null;
                            }
                        }
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void addParentUrlToCachePages(Set<String> daughterHrefs, String parentHref) {
        for (String href : daughterHrefs) {
            Page page = PageContainer.getPage(href);
            if (page != null) {
                page.getParentURLs().add(parentHref);
            } else {
                page = new Page(href);
                page.setParentURLs(new HashSet<>(Collections.singletonList(parentHref)));
                PageContainer.putPage(href, page);
            }
        }
    }

    private void putURLsToCache(Set<String> hrefs, String parentPageUrl) {
        hrefs.forEach(it -> {
            String itKey = GrabAllLinksHelper.generateKey(it);
            boolean containsLink = PageContainer.containsLink(itKey);
            if (!containsLink && !parentPageUrl.equals(it)) {
                Page website = PageContainer.getPage(it);
                if (website == null) {
                    website = new Page(it);
                    PageContainer.putPage(it, website);
                }
            }
        });
    }
}
