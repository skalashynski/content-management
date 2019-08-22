package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GrabAllLinksFlowTest {

    private Map<String, String> headers;

    private GrabAllLinksFlow flow;

    @Before
    public void init() throws InvalidJarRequestException {
        headers = new HashMap<>();
        headers.put("domain_name", "productiveedge.com");
        headers.put("url_protocol", "https");
        headers.put("url_port", "");
        headers.put("maximum_amount_internal_url_to_process", "10");
        headers.put("allow_redirect", "false");
        headers.put("url", "https://www.productiveedge.com/");


        flow = new GrabAllLinksFlow(new GrabAllLinksRequest(headers));
    }

    @Test
    public void extractHrefsFromWebsite() throws Exception {
        String productiveedge = "https://www.productiveedge.com/solutions/marketing-transformation";

        Set<String> hrefs = flow.extractHrefsFromWebsite(productiveedge);
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
        System.out.println(flow.run().toString());
    }

}