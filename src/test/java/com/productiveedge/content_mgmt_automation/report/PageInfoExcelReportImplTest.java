package com.productiveedge.content_mgmt_automation.report;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PageInfoExcelReportImplTest {

    private Page page = new Page("http://workfusion.com");
    private List<Tag> tags = new ArrayList<>();
    private String text = "Test text";

    @Before
    public void init() {
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[3]//footer//div//div//div[2]//div[2]", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[3]//footer//div//div//div[2]", null, text, null));

        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]//div//div//p", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]//div", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]//div//div", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]", null, text, null));

        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div[1]", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div[2]", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div[3]", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div[4]", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div[5]", null, text, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div[6]", null, text, null));
    }

    @Test
    public void findAreaByTagXPath() {
        tags.forEach(e -> page.addTag(e));
        Set<Page.PageArea> areas = page.getTextAreas().get(text);
        Assert.assertEquals(areas.size(), 8);
    }
}