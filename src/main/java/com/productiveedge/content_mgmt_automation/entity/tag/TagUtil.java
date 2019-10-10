package com.productiveedge.content_mgmt_automation.entity.tag;

public class TagUtil {
    public static String validateData(String data) {
        //return data.replaceAll("[\"]", "\\\\\"");
        return data.replaceAll("[\"]", "'");
    }
}
