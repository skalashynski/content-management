package com.productiveedge.content_mgmt_automation;

import com.productiveedge.content_mgmt_automation.entity.CommandFlowStrategy;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            String a[] = {
                    "ROOT_FOLDER_PATH=C://folder",
                    "url=https://www.productiveedge.com",
                    "domain_name=www.productiveedge.com",
                    "url_protocol=https",
                    "url_port=80",
                    "maximum_amount_internal_url_to_process=100",
                    "allow_redirect=false",
                    "generate_report=true",
                    "report_name=Page_report",
                    "take_screenshot=true"
            };
            Map<String, String> request = makeRequest(a);
            Queue<Flow> flows = CommandFlowStrategy.getFlow(request);
            while (!flows.isEmpty()) {
                Flow flow = flows.poll();
                flow.run();
            }
        } catch (InvalidJarRequestException ex) {
            logger.error(ex.getMessage());
        }


        //Response response = flow.run();
    }

    //works only if the key doesn't have any '='
    private static Map<String, String> makeRequest(String[] args) {
        Map<String, String> request = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String key = arg.substring(0, arg.indexOf('=')).toUpperCase();
                String value = arg.substring(arg.indexOf('=') + 1);
                request.put(key, value);
            }
        }
        return request;
    }
}
