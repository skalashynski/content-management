package com.productiveedge.content_mgmt_automation.entity.response;

import com.productiveedge.content_mgmt_automation.entity.Page;
import lombok.Data;

import java.util.Map;

@Data
public class GrabAllLinksResponse extends Response {
    private Map<String, Page> result;
}
