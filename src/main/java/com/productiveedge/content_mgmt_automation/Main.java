package com.productiveedge.content_mgmt_automation;

import com.productiveedge.content_mgmt_automation.entity.CommandFlowStrategy;
import com.productiveedge.content_mgmt_automation.entity.request.Request;
import com.productiveedge.content_mgmt_automation.entity.response.Response;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;


public class Main {
    public static void main(String[] args) throws InvalidJarRequestException {
        //String a[] = {"command=CREATE_FOLDERS", "ROOT_FOLDER_PATH=C://folder"};
        Request request = makeRequest(args);
        Flow flow = CommandFlowStrategy.getCommand(request);
        Response response = flow.run(request);
        System.out.println(response.toString());
    }

    //works only if the key doesn't have any '='
    private static Request makeRequest(String[] args) {
        Request request = new Request();
        for (String arg : args) {
            if (arg.contains("=")) {
                String key = arg.substring(0, arg.indexOf('='));
                String value = arg.substring(arg.indexOf('=') + 1).toUpperCase();
                request.put(key, value);
            }
        }
        return request;
    }
}
