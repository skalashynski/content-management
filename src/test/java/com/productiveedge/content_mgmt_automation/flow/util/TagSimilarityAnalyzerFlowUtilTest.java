package com.productiveedge.content_mgmt_automation.flow.util;

import com.google.gson.Gson;
import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.CompoundTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TagSimilarityAnalyzerFlowUtilTest {

    private List<Tag> tags = new ArrayList<>();

    @Before
    public void init() {
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[3]//footer//div//div//div[2]//div[2]", null, null, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[3]//footer//div//div//div[2]", null, null, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]//div//div//p", null, null, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]//div", null, null, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]//div//div", null, null, null));
        tags.add(new BaseTag(null, null, null, "//body//main//div[2]//div[2]//section[4]//div//div//div[2]//div//div[5]", null, null, null));
    }

    @Test
    public void compact() {
        List<CompoundTag> res = TagSimilarityAnalyzerFlowUtil.groupByBlock(tags);
        Gson gson = new Gson();
        String json = gson.toJson(res);
        System.out.println(json);

    }
}