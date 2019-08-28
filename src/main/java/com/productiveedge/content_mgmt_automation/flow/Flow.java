package com.productiveedge.content_mgmt_automation.flow;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;


public interface Flow {
    void run() throws InvalidJarRequestException;
}
