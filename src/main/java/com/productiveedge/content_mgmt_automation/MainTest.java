package com.productiveedge.content_mgmt_automation;

import com.productiveedge.content_mgmt_automation.flow.CommandFlowStrategy;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args) {
        try {
            String[] a = {
                    "root_folder_path=C://folder",
                    //"driver_path=C://folder//chromedriver.exe",
                    "driver_path=C://folder//IEDriverServer.exe",
                    "page_domain_url=https://www.workfusion.com/careers/",
                    "max_process_urls_value=1",

                    "generate_links_per_page_report=true",
                    "save_html=false",
                    "save_txt=false",
                    "take_screenshot=true",

                    "page_screen_space_value=550",
                    //"browser_name=chrome",
                    "browser_name=EXPLORER",
                    "process_strange_urls=false",

                    /* her you can set particular tags comma separated.
                        for example {div, a, p}
                        ro set value 'ALL'
                    * */
                    "TAGS_TO_ANALYZE=all"
            };

            Map<String, String> request = makeRequest(a);
            Queue<Flow> flows = CommandFlowStrategy.getFlow(request);
            while (!flows.isEmpty()) {
                Flow flow = flows.poll();
                flow.run();
            }
        } catch (InvalidJarRequestException ex) {
            logger.error(ex.getMessage());
        }
    }

    //works only if the key doesn't have any '='
    private static Map<String, String> makeRequest(String[] args) {
        Map<String, String> request = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String key = arg.substring(0, arg.indexOf('=')).toUpperCase();
                String value = arg.substring(arg.indexOf('=') + 1);
                request.put(key, value);
            }
        }
        return request;
    }
}
