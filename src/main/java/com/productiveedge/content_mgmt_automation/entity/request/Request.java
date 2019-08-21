package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.entity.Command;
import lombok.Data;

import java.util.HashMap;

@Data
public class Request extends HashMap<String, String>  {
    protected Command command;
}
