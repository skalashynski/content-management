package com.productiveedge.content_mgmt_automation.entity;

import com.productiveedge.content_mgmt_automation.entity.request.CreateLocalFolderRequest;
import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.CreateLocalFoldersFlow;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.GrabAllLinksFlow;

import java.util.Map;

public class CommandFlowStrategy {

    private CommandFlowStrategy() {

    }

    public static Flow getFlow(Map<String, String> request) throws InvalidJarRequestException {
        Command command = Command.valueOf(request.get("command"));
        switch (command) {
            case CREATE_FOLDERS:{
                return new CreateLocalFoldersFlow(new CreateLocalFolderRequest(request));
            }

            case GRAB_ALL_LINKS:{
                return new GrabAllLinksFlow(new GrabAllLinksRequest(request));
            }
        }
        return null;
    }
}
