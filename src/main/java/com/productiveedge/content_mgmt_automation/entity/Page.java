package com.productiveedge.content_mgmt_automation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Page {
    private String url;
    private boolean isProcessed;
    private Status status;
    private String messageDescription;
    private Set<String> emailHrefs;
    private Set<String> externalHrefs;
    private Set<String> internalHrefs;
    private Set<String> pdfHrefs;
    private Set<String> pngHrefs;
    private Set<String> parentURLs;

    public enum Status {
        REDIRECT_OR_INVALID_URL, PROCESSED, UNPROCESSED,
    }

    public Page(String url) {
        this.url = url;
        isProcessed = false;
        status = Status.UNPROCESSED;
        emailHrefs = new HashSet<>();
        internalHrefs = new HashSet<>();
        pdfHrefs = new HashSet<>();
        pngHrefs = new HashSet<>();
        parentURLs = new HashSet<>();
        messageDescription = "";
    }
}
