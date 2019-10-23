package com.productiveedge.content_mgmt_automation.entity;

import com.productiveedge.content_mgmt_automation.service.ApacheHttpClient;
import com.productiveedge.content_mgmt_automation.service.exception.ApacheHttpClientException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@Ignore
public class ApacheHttpClientTest {

    @Test(expected = ApacheHttpClientException.class)
    public void blockRedirectSendGet() throws ApacheHttpClientException {
        String url = "https://www.productiveedge.com/blog";
        Map<String, String> headers = new HashMap<>();
        headers.put("allow_redirect", "false");
        //ApacheHttpClient.sendGet(url, headers);
    }

    @Test()
    public void allowRedirectSendGet() throws ApacheHttpClientException {
        String url = "https://www.productiveedge.com";
        String html = ApacheHttpClient.sendGet(url, null);
        assertNotNull(html);
        System.out.println(html);
    }
}