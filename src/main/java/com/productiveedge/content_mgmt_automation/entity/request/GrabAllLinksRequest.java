package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.util.Map;

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

    public GrabAllLinksRequest(Map<String, String> request) throws InvalidJarRequestException{
        validate(request);
        this.url = request.get(REQUEST_PARAMETER.URL.name().toLowerCase());
        this.domainName = request.get(REQUEST_PARAMETER.DOMAIN_NAME.name().toLowerCase());
        this.urlProtocol = request.get(REQUEST_PARAMETER.URL_PROTOCOL.name().toLowerCase());
        this.urlPort = request.get(REQUEST_PARAMETER.URL_PORT.name().toLowerCase());
        this.processUrlCount = request.get(REQUEST_PARAMETER.MAXIMUM_AMOUNT_INTERNAL_URL_TO_PROCESS.name().toLowerCase());
        this.allowRedirect = request.get(REQUEST_PARAMETER.ALLOW_REDIRECT.name().toLowerCase());
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (GrabAllLinksRequest.REQUEST_PARAMETER parameter : GrabAllLinksRequest.REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toLowerCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request");
            }
        }
    }
}
