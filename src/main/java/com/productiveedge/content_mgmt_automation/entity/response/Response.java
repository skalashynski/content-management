package com.productiveedge.content_mgmt_automation.entity.response;

import lombok.Data;

import java.util.HashMap;

@Data
public class Response extends HashMap{
    protected String description;
    protected String code;

}
