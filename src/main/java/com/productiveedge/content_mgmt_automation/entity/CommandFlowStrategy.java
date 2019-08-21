package com.productiveedge.content_mgmt_automation.entity;

import com.productiveedge.content_mgmt_automation.flow.impl.CreateLocalFoldersFlow;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.GrabAllLinksFlow;

import java.util.Map;

public class CommandFlowStrategy {

    private CommandFlowStrategy() {

    }

    public static Flow getCommand(Map<String, String> request) {
        Command command = Command.valueOf(request.get("command"));
        switch (command) {
            case CREATE_FOLDERS:
                return new CreateLocalFoldersFlow();
            case GRAB_ALL_LINKS:
                return new GrabAllLinksFlow();
        }
        return null;
    }
}
