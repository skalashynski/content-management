package com.productiveedge.content_mgmt_automation.flow;

import com.productiveedge.content_mgmt_automation.entity.request.*;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CommandFlowStrategy {

    private CommandFlowStrategy() {

    }

    public static Queue<Flow> getFlow(Map<String, String> request) throws InvalidJarRequestException {
        Queue<Flow> queue = new LinkedList<>();
        queue.add(new CreateLocalFoldersFlow(new CreateLocalFolderRequest(request)));
        queue.add(new GrabAllLinksFlow(new GrabAllLinksRequest(request)));
        if (request.get(Command.GENERATE_REPORT.name()) != null && Boolean.valueOf(request.get(Command.GENERATE_REPORT.name()))) {
            queue.add(new GenerateExcelReportFlow(new GenerateExcelReportRequest(request)));
        }
        if (request.get(Command.SAVE_HTML.name()) != null && Boolean.valueOf(request.get(Command.SAVE_HTML.name()))) {
            queue.add(new SaveHtmlFlow(new SaveHtmlRequest(request)));
        }
        if (request.get(Command.SAVE_TXT.name()) != null && Boolean.valueOf(request.get(Command.SAVE_TXT.name()))) {
            queue.add(new SaveTxtFlow(new SaveTxtRequest(request)));
        }
        if (request.get(Command.TAKE_SCREENSHOT.name()) != null && Boolean.valueOf(request.get(Command.TAKE_SCREENSHOT.name()))) {
            queue.add(new TakeScreenshotFlow(new TakeScreenshotRequest(request)));
        }
        queue.add(new TextSimilarityAnalyzerFlow(new TextSimilarityAnalyzerRequest(request)));
        return queue;
    }

    public enum Command {
        SAVE_HTML, SAVE_TXT, TAKE_SCREENSHOT, GENERATE_REPORT
    }
}
