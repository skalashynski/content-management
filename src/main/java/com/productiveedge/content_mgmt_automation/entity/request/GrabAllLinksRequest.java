package com.productiveedge.content_mgmt_automation.entity.request;

import lombok.Data;

@Data
public class GrabAllLinksRequest extends Request {
    public enum REQUEST_PARAMETER {
        URL, DOMAIN_NAME, URL_PROTOCOL, URL_PORT, MAXIMUM_AMOUNT_INTERNAL_URL_TO_PROCESS, ALLOW_REDIRECT
    }

    private String url;
    private String domainName;
    private String urlProtocol;
    private String urlPort;
    private String processUrlCount;
    private String allowRedirect;
}
