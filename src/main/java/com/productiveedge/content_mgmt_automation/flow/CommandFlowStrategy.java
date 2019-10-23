package com.productiveedge.content_mgmt_automation.flow;

import com.productiveedge.content_mgmt_automation.entity.request.*;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiPredicate;

public class CommandFlowStrategy {

    private static BiPredicate<Map<String, String>, Command> fun = (request, command) -> request.get(command.name()) != null && Boolean.valueOf(request.get(command.name()));


    private CommandFlowStrategy() {

    }

    public static Queue<Flow> getFlow(Map<String, String> request) throws InvalidJarRequestException {
        Queue<Flow> queue = new LinkedList<>();
        queue.add(new FoldersCreatorFlow(new CreateLocalFolderRequest(request)));
        queue.add(new PageInfoCollectorFlowImpl(new GrabAllLinksRequest(request)));
        if (fun.test(request, Command.GENERATE_LINKS_PER_PAGE_REPORT)) {
            queue.add(new PageInfoReportGeneratorFlow(new GenerateExcelReportRequest(request)));
        }
        if (fun.test(request, Command.SAVE_HTML)) {
            queue.add(new HtmlFileGeneratorFlow(new SaveHtmlRequest(request)));
        }
        if (fun.test(request, Command.SAVE_TXT)) {
            queue.add(new TxtFileGeneratorFlow(new SaveTxtRequest(request)));
        }
        if (fun.test(request, Command.TAKE_SCREENSHOT)) {
            queue.add(new PngScreenshotFileGeneratorFlow(new TakeScreenshotRequest(request)));
        }
        queue.add(new TextUniquenessAnalyzerFlow(new TextSimilarityAnalyzerRequest(request)));
        return queue;
    }

    public enum Command {
        SAVE_HTML, SAVE_TXT, TAKE_SCREENSHOT, GENERATE_LINKS_PER_PAGE_REPORT
    }
}
