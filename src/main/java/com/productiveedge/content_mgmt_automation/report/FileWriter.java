package com.productiveedge.content_mgmt_automation.report;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileWriter {
    public static void write(String filePath, String text) throws IOException {
        FileUtils.writeStringToFile(new File(filePath), text, StandardCharsets.UTF_8);
    }
}
