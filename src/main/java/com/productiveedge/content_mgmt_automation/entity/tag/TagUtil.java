package com.productiveedge.content_mgmt_automation.entity.tag;

public class TagUtil {
    public static String validateData(String data) {
        if (data != null) {
            return data.replaceAll("[\"]", "'");
        }
        return null;

    }
}
