package com.productiveedge.content_mgmt_automation.entity;

import com.productiveedge.content_mgmt_automation.entity.request.CreateLocalFolderRequest;
import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.entity.request.TakeScreenshotRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.GenerateExcelReportFlow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.CreateLocalFoldersFlow;
import com.productiveedge.content_mgmt_automation.flow.impl.GrabAllLinksFlow;
import com.productiveedge.content_mgmt_automation.flow.impl.TakeScreenshotFlow;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CommandFlowStrategy {

    private static final String COMMAND_PARAMETER = "COMMAND";

    private CommandFlowStrategy() {

    }

    public static Queue<Flow> getFlow(Map<String, String> request) throws InvalidJarRequestException {
        Queue<Flow> queue = new LinkedList<>();
        queue.add(new CreateLocalFoldersFlow(new CreateLocalFolderRequest(request)));
        queue.add(new GrabAllLinksFlow(new GrabAllLinksRequest(request)));
        if (request.get("GENERATE_REPORT") != null && Boolean.valueOf(request.get("GENERATE_REPORT"))) {
            queue.add(new GenerateExcelReportFlow());
        }
        if (request.get("save_html") != null) {
            //queue.add()
        }
        if (request.get("save_txt") != null) {
            //queue.add()
        }
        if (request.get("take_screenshot") != null) {
            queue.add(new TakeScreenshotFlow(new TakeScreenshotRequest(request)));
        }
        return queue;
    }
}
