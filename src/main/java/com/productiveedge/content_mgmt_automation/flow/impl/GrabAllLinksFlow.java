package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.Page;
import com.productiveedge.content_mgmt_automation.entity.PageBuilder;
import com.productiveedge.content_mgmt_automation.entity.PageContainer;
import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.entity.response.GrabAllLinksResponse;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLInksHelper;
import com.productiveedge.content_mgmt_automation.service.ApacheHttpClient;
import com.productiveedge.content_mgmt_automation.service.exception.ApacheHttpClientException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.productiveedge.content_mgmt_automation.entity.Page.Status.PROCESSED;
import static com.productiveedge.content_mgmt_automation.entity.Page.Status.REDIRECT_OR_INVALID_URL;

public class GrabAllLinksFlow implements Flow<GrabAllLinksResponse, GrabAllLinksRequest> {

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

    private static boolean isRubbishHref(String href) {
        return isPhoneNumberHref.or(isAnchorHref).or(isJavascriptHref).test(href);
    }


    /**
     * this condition is used to avoid redirect pages fot URL which ends with '/'
     */
    private String convertURLKey(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    public static String convertUrlToCacheKey(String urlLink) throws MalformedURLException {
        URL url = new URL(urlLink);
        return getDomain(urlLink) + url.getPath();
    }


    private String createHomePageUrl(Map<String, String> request) {
        String homePageUrl = request.get(GrabAllLinksRequest.REQUEST_PARAMETER.URL.name().toLowerCase());
        if (homePageUrl == null) {
            homePageUrl = request.get(GrabAllLinksRequest.REQUEST_PARAMETER.URL_PROTOCOL.name().toLowerCase()) + "://" + request.get(GrabAllLinksRequest.REQUEST_PARAMETER.DOMAIN_NAME.name().toLowerCase());
            if (!"".equals(request.get(GrabAllLinksRequest.REQUEST_PARAMETER.URL_PORT.name().toLowerCase()))) {
                homePageUrl = homePageUrl + ":" + request.get(GrabAllLinksRequest.REQUEST_PARAMETER.URL_PORT.name().toLowerCase());
            }
        }
        return homePageUrl;
    }


    @Override
    public void validateClientRequest(GrabAllLinksRequest request) throws InvalidJarRequestException {
        for (GrabAllLinksRequest.REQUEST_PARAMETER parameter : GrabAllLinksRequest.REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toLowerCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request");
            }
        }
    }

    private String validateWebsiteURL(String grabbedHref, String websiteURLIterator) throws MalformedURLException {
        grabbedHref = grabbedHref.trim().toLowerCase();
        if (isRubbishHref(grabbedHref)) {
            grabbedHref = websiteURLIterator;
        }
        URL mergedURL = new URL(new URL(websiteURLIterator), grabbedHref);
        return mergedURL.toString();
    }

    private String cutOffURLParameters(String absoluteUrl, String relativeUrl) {
        try {
            URL mergedURL = new URL(new URL(absoluteUrl), relativeUrl);
            String url = mergedURL.toString();
            if (relativeUrl.contains(QUESTION_SYMBOL)) {
                mergedURL = new URL(url.substring(0, url.indexOf(QUESTION_SYMBOL)));
                url = mergedURL.toString();
            }
            if (relativeUrl.contains(ANCHOR_SYMBOL)) {
                mergedURL = new URL(url.substring(0, url.indexOf(ANCHOR_SYMBOL)));
                url = mergedURL.toString();
            }
            if (mergedURL.toString().endsWith(ANCHOR_SYMBOL)) {
                mergedURL = new URL(url.substring(0, url.length() - 1));
                url = mergedURL.toString();
            }
            return url;
        } catch (Exception e) {
  /*          logStatus << "Error during validating daughter url: " + websiteURL + "\r\n";
            logStatus << "Parent url: " + websiteURLIterator + "\r\n";
            logStatus << e.getMessage() + "\r\n";*/
        }
        return null;
    }

    private Set<String> getInternalHrefs(Set<String> daughterHrefs, String parentUrl) throws MalformedURLException {
        URL url = null;
        final String parentDomain = getDomain(parentUrl);
        return daughterHrefs
                .stream()
                .filter(it -> {
                            try {
                                String daughterDomain = getDomain(it);
                                return parentDomain.equals(daughterDomain);
                            } catch (MalformedURLException e) {
                                return false;
                            }
                        }
                ).collect(Collectors.toSet());
    }

    private static String getDomain(String url) throws MalformedURLException {
        String domain = new URL(url).getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private Set<String> getExternalHrefs(Set<String> hrefs, Set<String> internalHrefs) {
        Set<String> difference = new HashSet<>(hrefs);
        difference.removeAll(internalHrefs);
        return difference;
    }


    private Set<String> getHrefs(Set<String> hrefs, Predicate<String> predicate) {
        Set<String> emailHrefSet = hrefs
                .stream()
                .filter(predicate)
                .collect(Collectors.toCollection(TreeSet::new));
        hrefs.removeAll(emailHrefSet);
        return emailHrefSet;
    }

    private Set<String> convertInternalHrefs(Set<String> internalHrefSet, String pageUrl) {
        return internalHrefSet
                .stream()
                .map(e -> cutOffURLParameters(pageUrl, e))
                .collect(Collectors.toSet());
    }

    private void initPageContainer(Map<String, String> request) {
        //название метода не валидно
        //да и сам логика не валидна
        String homePageUrl = createHomePageUrl(request);
        String key = convertURLKey(homePageUrl);
        Page page = new Page(homePageUrl);
        PageContainer.put(key, page);
    }

    private Page processPage(Page page , Map<String, String> request) throws MalformedURLException, ApacheHttpClientException {
        String webPageUrl = page.getUrl();
        Set<String> allValidHrefSet = extractHrefsFromWebsite(webPageUrl, request);
        Set<String> emailHrefSet = getHrefs(allValidHrefSet, isMailHref);
        Set<String> pdfHrefSet = getHrefs(allValidHrefSet, isPdfHref);
        Set<String> pngHrefSet = getHrefs(allValidHrefSet, isPictureHref);
        Set<String> internalHrefSet = getInternalHrefs(allValidHrefSet, webPageUrl);
        Set<String> externalHrefSet = getExternalHrefs(allValidHrefSet, internalHrefSet);
        internalHrefSet = convertInternalHrefs(internalHrefSet, webPageUrl);

        addParentUrlToWebsites(internalHrefSet, webPageUrl);
        addInternalLinkToUnprocessedCache(internalHrefSet, webPageUrl);

        //website = PageContainer.getPage(cacheKeyURL);
        PageBuilder pageBuilder;
        if (page != null) {
            pageBuilder = new PageBuilder(page);
        } else {
            pageBuilder = new PageBuilder();
        }
        page = pageBuilder.setUrl(webPageUrl)
                .setProcessed(true)
                .setStatus(PROCESSED)
                .setEmailHrefs(emailHrefSet)
                .setExternalHrefs(externalHrefSet)
                .setInternalHrefs(internalHrefSet)
                .setPdfHrefs(pdfHrefSet)
                .setPngHrefs(pngHrefSet)
                .build();
        return page;
    }


    @Override
    public GrabAllLinksResponse run(GrabAllLinksRequest request) throws InvalidJarRequestException {
        validateClientRequest(request);

        initPageContainer(request);


        int processCount = Integer.valueOf(request.get(GrabAllLinksRequest.REQUEST_PARAMETER.MAXIMUM_AMOUNT_INTERNAL_URL_TO_PROCESS.name().toLowerCase()));
        Page page;

        while (PageContainer.isUnprocessedPageExist() && PageContainer.processedCacheWebsitesCount() < processCount) {
            Map.Entry<String, Page> unprocessedPageEntry = PageContainer.getUnprocessedPageEntry();
            String cacheKeyURL = unprocessedPageEntry.getKey();
            page = unprocessedPageEntry.getValue();
            try {
                page = processPage(page, request);
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());

            } catch (ApacheHttpClientException e) {
                page = new Page(page.getUrl());
                page.setProcessed(true);
                page.setStatus(REDIRECT_OR_INVALID_URL);
            } finally {
                PageContainer.put(cacheKeyURL, page);
            }
        }
        GrabAllLinksResponse response = new GrabAllLinksResponse();
        response.setResult(PageContainer.getCache());
        return response;
    }

    public Set<String> extractHrefsFromWebsite(String url, Map<String, String> headers) throws ApacheHttpClientException {
        String html = ApacheHttpClient.sendGet(url, headers, null);
        Set<String> hrefs = GrabAllLInksHelper.extractHtmlHrefs(html);
        return hrefs
                .stream()
                .map(it -> {
                            try {
                                return validateWebsiteURL(it, url);
                            } catch (MalformedURLException e) {
                                return null;
                            }
                        }
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void addParentUrlToWebsites(Set<String> daughterHrefs, String parentHref) {
        for (String href : daughterHrefs) {
            Page website = PageContainer.getPage(convertURLKey(href));
            if (website != null) {
                website.getParentURLs().add(parentHref);
            } else {
                website = new Page(href);
                website.setParentURLs(new HashSet<>(Collections.singletonList(parentHref)));
                PageContainer.put(convertURLKey(href), website);
            }
        }
    }

    private void addInternalLinkToUnprocessedCache(Set<String> internalLinks, String websiteURLIterator) {
        internalLinks.forEach(it -> {
            try {
                String itPath = convertUrlToCacheKey(it);
                boolean containsLink = PageContainer.containsLink(itPath);
                if (!containsLink && !websiteURLIterator.equals(it)) {
                    Page website = PageContainer.getPage(convertURLKey(it));
                    if (website == null) {
                        website = new Page(it);
                        PageContainer.put(convertURLKey(it), website);
                    }
                }
            } catch (MalformedURLException e) {

            }
        });
    }
}
