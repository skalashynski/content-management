package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.service.exception.ApacheHttpClientException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GrabAllLinksFlowTest {

    @Test
    public void extractHrefsFromWebsite() throws ApacheHttpClientException {
        String productiveedge = "https://www.productiveedge.com/solutions/marketing-transformation";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("allow_redirect", "false");

        Set<String> hrefs = new GrabAllLinksFlow().extractHrefsFromWebsite(productiveedge, requestHeaders);
        System.out.println(hrefs);
    }

    @Test
    public void test() throws Exception {
        String productiveedge = "https://www.productiveedge.com/solutions/marketing-transformation";
        //String res = ApacheHttpClient.sendGet(productiveedge, null, null);
        // System.out.println(res);
    }

    @Test
    public void run() throws Exception {
       GrabAllLinksRequest requestHeaders = new GrabAllLinksRequest();

        requestHeaders.put("domain_name", "productiveedge.com");
        requestHeaders.put("url_protocol", "https");
        requestHeaders.put("url_port", "");
        requestHeaders.put("maximum_amount_internal_url_to_process", "10");
        requestHeaders.put("allow_redirect", "false");
        requestHeaders.put("url", "https://www.productiveedge.com/");
        System.out.println(new GrabAllLinksFlow().run(requestHeaders).toString());
    }

}