package com.productiveedge.content_mgmt_automation.flow;

import com.productiveedge.content_mgmt_automation.entity.response.Response;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;


public interface Flow<K extends Response> {
    K run() throws InvalidJarRequestException;
}
