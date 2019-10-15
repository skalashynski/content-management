package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.tag.BaseTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TextSimilarityAnalyzerFlowTest {

    private List<Tag> tags = new ArrayList<>();
    private static final String IFRAME_TAG_NAME = "iframe";
    private static final String NOSCRIPT_TAG_NAME = "noscript";
    private static final String ROOT_TAG_NAME = "#root";
    private static final String[] BAD_TAGS = {IFRAME_TAG_NAME, NOSCRIPT_TAG_NAME, ROOT_TAG_NAME};


    private final Predicate<BaseTag> badTagsFilter = tag -> !tag.getTextContent().isEmpty() &&
            !tag.getName().equalsIgnoreCase(IFRAME_TAG_NAME) &&
            !tag.getName().equalsIgnoreCase(NOSCRIPT_TAG_NAME) &&
            !tag.getName().equalsIgnoreCase(ROOT_TAG_NAME);

    private final Predicate<BaseTag> fun = tag -> !tag.getTextContent().isEmpty() && Arrays.stream(BAD_TAGS).noneMatch(e -> e.equalsIgnoreCase(tag.getName()));



    @Before
    public void init() {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",\"");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                    values[i] = values[i].startsWith("\"") ? values[i].substring(1) : values[i];
                    values[i] = values[i].endsWith("\"") ? values[i].substring(0, values[i].length() - 1) : values[i];
                }
                tags.add(new BaseTag(values[0], values[1], values[2], values[3], values[4], values[5], ""));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void compactGroupBasedOnTextContent() throws Exception {
        //List<Tag> res = TagSimilarityAnalyzerFlowUtil.compactGroupBasedOnTextContent(tags);
        //new TagCsvRepository("compactGroupBasedOnTextContent-data-test-result.csv").saveAll(res);

    }

    @Test
    public void testPredicate() {
        System.out.println(badTagsFilter.equals(fun));
    }
}