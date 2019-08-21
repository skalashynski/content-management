package com.productiveedge.content_mgmt_automation.flow;

import com.productiveedge.content_mgmt_automation.entity.request.Request;
import com.productiveedge.content_mgmt_automation.entity.response.Response;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;


public interface Flow<K extends Response, T extends Request> {
    K run(T request) throws InvalidJarRequestException;

    void validateClientRequest(T request) throws InvalidJarRequestException;
}
