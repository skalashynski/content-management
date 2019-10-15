package com.productiveedge.content_mgmt_automation.entity.page;

import java.util.Set;

public class PageBuilder {
    private final Page page;

    public PageBuilder() {
        this.page = new Page();
    }

    public PageBuilder(Page page) {
        this.page = page;
    }

    public PageBuilder setUrl(String url) {
        page.setUrl(url);
        return this;
    }

    public PageBuilder setProcessed(boolean isProcessed) {
        page.setProcessed(isProcessed);
        return this;
    }

    public PageBuilder setStatus(Page.Status status) {
        page.setStatus(status);
        return this;
    }

    public PageBuilder setEmailHrefs(Set<String> emailHrefs) {
        page.setEmailHrefs(emailHrefs);
        return this;
    }

    public PageBuilder setExternalHrefs(Set<String> externalHrefs) {
        page.setExternalHrefs(externalHrefs);
        return this;
    }

    public PageBuilder setInternalHrefs(Set<String> internalHrefs) {
        page.setInternalHrefs(internalHrefs);
        return this;
    }

    public PageBuilder setPdfHrefs(Set<String> pdfHrefs) {
        page.setPdfHrefs(pdfHrefs);
        return this;
    }

    public PageBuilder setPngHrefs(Set<String> pngHrefs) {
        page.setPngHrefs(pngHrefs);
        return this;
    }

    public PageBuilder setParentHrefs(Set<String> parentHrefs) {
        page.setParentURLs(parentHrefs);
        return this;
    }

    public PageBuilder setHtmlContent(String htmlContent) {
        page.setHtmlContent(htmlContent);
        return this;
    }

    public PageBuilder setTxtContent(String txtContent) {
        page.setTxtContent(txtContent);
        return this;
    }

    public Page build() {
        return page;
    }
}
