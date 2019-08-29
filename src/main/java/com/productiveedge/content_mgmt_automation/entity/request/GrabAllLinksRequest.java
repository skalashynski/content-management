package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class GrabAllLinksRequest extends Request {
    public enum REQUEST_PARAMETER {
        PROCESS_URL, MAX_PROCESS_URLS_VALUE, PROCESS_STRANGE_URLS, PROCESS_STRATEGY
    }

    private String processUrl;
    private int processUrlCount;
    private String allowRedirect;
    private boolean startFromIndexPage;
    private String processStrategy;

    public GrabAllLinksRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.processUrl = request.get(REQUEST_PARAMETER.PROCESS_URL.name());
        this.processStrategy = request.get(REQUEST_PARAMETER.PROCESS_STRATEGY.name());
        try {
            this.processUrlCount = Integer.parseInt(request.get(REQUEST_PARAMETER.MAX_PROCESS_URLS_VALUE.name()));
        } catch (NumberFormatException e) {
            throw new InvalidJarRequestException("Incorrect request parameter");
        }
        this.allowRedirect = request.get(REQUEST_PARAMETER.PROCESS_STRANGE_URLS.name());
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (REQUEST_PARAMETER parameter : REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in request to grab hrefs in page.");
            }
        }
    }
}
