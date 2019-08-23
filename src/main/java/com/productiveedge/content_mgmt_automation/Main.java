package com.productiveedge.content_mgmt_automation;

import com.productiveedge.content_mgmt_automation.entity.CommandFlowStrategy;
import com.productiveedge.content_mgmt_automation.entity.response.Response;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws InvalidJarRequestException {
        //String a[] = {"command=CREATE_FOLDERS", "ROOT_FOLDER_PATH=C://folder"};
        Map<String, String> request = makeRequest(args);
        Flow flow = CommandFlowStrategy.getFlow(request);
        Response response = flow.run();
        System.out.println(response.toString());
    }

    //works only if the key doesn't have any '='
    private static Map<String, String> makeRequest(String[] args) {
        Map<String, String> request = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String key = arg.substring(0, arg.indexOf('=')).toUpperCase();
                String value = arg.substring(arg.indexOf('=') + 1).toUpperCase();
                request.put(key, value);
            }
        }

        return request;
    }
}
