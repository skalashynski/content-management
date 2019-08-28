package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;

import java.util.Map;

@Data
public abstract class Request {

    public abstract void validate(Map<String, String> request) throws InvalidJarRequestException;
}
