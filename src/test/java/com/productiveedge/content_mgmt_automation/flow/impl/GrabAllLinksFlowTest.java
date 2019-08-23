package com.productiveedge.content_mgmt_automation.flow.impl;


import com.productiveedge.content_mgmt_automation.entity.PageContainer;
import com.productiveedge.content_mgmt_automation.entity.request.GrabAllLinksRequest;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.GrabAllLinksExcelRepository;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class GrabAllLinksFlowTest {

    private Map<String, String> headers;

    private GrabAllLinksFlow flow;

    @Before
    public void init() throws InvalidJarRequestException {
        headers = new HashMap<>();
        headers.put("domain_name", "productiveedge.com");
        headers.put("url_protocol", "https");
        headers.put("url_port", "");
        headers.put("maximum_amount_internal_url_to_process", "100");
        headers.put("allow_redirect", "false");
        headers.put("url", "https://www.productiveedge.com/");


        flow = new GrabAllLinksFlow(new GrabAllLinksRequest(headers));
    }

    @Test
    public void extractHrefsFromWebsite() throws Exception {
        String productiveedge = "https://www.productiveedge.com/solutions/marketing-transformation";

        //Set<String> hrefs = flow.extractHrefsFromWebsite(productiveedge);
        //System.out.println(hrefs);
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


    @Test
    public void saveAll() throws Exception {
        System.out.println(flow.run().toString());
        GrabAllLinksExcelRepository.saveAll(PageContainer.getCache().values());
    }



    @Test
    public void createHomePage() throws Exception {
        String[][] data = new String[][]{
                {"https://www.instagram.com/friends/1", "https", "instagram.com", "80", ""},
                {"https://www.instagram.com/friends/1/", "instagram.com/friends/1"},
                {"http://www.instagram.com/friends/1", "instagram.com/friends/1"},
                {"www.instagram.com/friends/1", "instagram.com/friends/1"},
                {"https://instagram.com/friends/1", "instagram.com/friends/1"},
                {"https://www.instagram.com/friends/1?message=1", "instagram.com/friends/1"},
                {"https://vk.com/feed", "vk.com/feed"},
                {"https://sitechecker.pro/what-is-url/", "sitechecker.pro/what-is-url"},
                {"https://www.instagram.com/friends/1#message", "instagram.com/friends/1"},
        };
        for (String[] datum : data) {
            String res = GrabAllLinksHelper.createHomePageUrl(datum[0], datum[1], datum[2], datum[3]);
            assertEquals(res, datum[1]);
        }
    }

    @Test
    public void convertInternalHrefs() {
        String parentHref = "https://instagram.com/overal/friends";
        String[][] data = new String[][]{
                //regarding root of the site
                {"/brad", "https://instagram.com/brad"},
                {"/patricia?who", "https://instagram.com/patricia"},

                {"patric", "https://instagram.com/overal/patric"},
                {"leon?who", "https://instagram.com/overal/leon"}
        };

        for (int i = 0; i < data.length; i++) {
            //String res = GrabAllLinksFlow.convertInternalHref.apply(data[i][0], parentHref);
           // System.out.println(res);
           // assertEquals("" + i, res, data[i][1]);
        }
    }

    @Test
    public void concatURLs() throws Exception {
        String[][] data = new String[][]{
                //regarding root of the site
                {"https://www.instagram.com/friends/overal", "/pretty/1", "https://www.instagram.com/pretty/1"},
                {"https://www.instagram.com/friends/overal/", "/pretty/1", "https://www.instagram.com/pretty/1"},

                {"https://www.instagram.com/friends/", "pretty/1", "https://www.instagram.com/friends/pretty/1"},
                {"https://www.instagram.com/friends", "pretty/1", "https://www.instagram.com/pretty/1"},
                {"https://www.instagram.com/friends/overal", "../pretty/1", "https://www.instagram.com/pretty/1"},
                {"https://www.instagram.com/friends/overal/", "../pretty/1", "https://www.instagram.com/friends/pretty/1"},
                //{"https://www.instagram.com/friends/overal/", "../pretty/1", "https://www.instagram.com/friends/pretty/1"},
        };
        for (int i = 0; i < data.length; i++) {
            URL res = GrabAllLinksFlow.concatURLs(data[i][0], data[i][1]);
            System.out.println(res.toString());
            assertEquals("" + i, res.toString(), data[i][2]);
        }
    }

    @Test
    public void cutOffURLParameters() throws Exception {
        String[][] data = new String[][]{
                //regarding root of the site
                {"https://www.instagram.com/friends/overal?name=Patric", "https://www.instagram.com/friends/overal"},
                {"https://www.instagram.com/friends/overal/#", "https://www.instagram.com/friends/overal/"},
                {"https://www.instagram.com/friends/?name=Patric&firstname=", "https://www.instagram.com/friends/"},
                {"https://www.instagram.com/friends/overal#message", "https://www.instagram.com/friends/overal"},
        };
        for (int i = 0; i < data.length; i++) {
            String res = GrabAllLinksFlow.cutOffURLParameters(data[i][0]);
            System.out.println(res);
            assertEquals("" + i, res, data[i][1]);
        }
    }

    @Test
    public void isAbsoluteHref() {
        String[][] data = new String[][]{
                {"https://www.instagram.com/friends/overal"},
                {"https://www.instagram.com/friends/overal/"},
                {"friends/pretty/1"},
                {"/pretty/1"},
        };
        for (String[] datum : data) {
            System.out.println(GrabAllLinksFlow.isAbsoluteHref(datum[0]));
        }
    }

}