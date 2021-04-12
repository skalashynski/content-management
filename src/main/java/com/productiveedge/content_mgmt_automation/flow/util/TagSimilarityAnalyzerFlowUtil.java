package com.productiveedge.content_mgmt_automation.flow.util;

import com.productiveedge.content_mgmt_automation.entity.page.Page;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * the implementation was rewrote. See Page.PageArea class
 */

@Deprecated
public class TagSimilarityAnalyzerFlowUtil {

    private static final Comparator<Tag> descTagXpathSorting = Comparator.comparing(Tag::getFullTagXPath);

    /**
     * For example tag has fullTagXpath like
     * //body//main//div[2]//div[3]//footer//div//div//div[1]//div[5]//div//div[2]//div[5]
     * And another one has fullTagXpath like
     * //body//main//div[2]//div[3]//footer//div//div//div[1]//div[5]//div//div[2]//div[5]//a
     * <p>
     * As result these tags will be compacted to map where key is least-length value, value - List of Tags.
     * @return Map<String, List < Tag>>
     */

    public static List<Page.PageArea> groupByBlock(List<Tag> pageTags) {
        List<Page.PageArea> res = new ArrayList<>();
        List<Tag> sorted = pageTags.stream()
                .sorted(descTagXpathSorting.reversed())
                .collect(Collectors.toList());
        int i = 0;
        do {
            List<Tag> entryValue = new ArrayList<>();
            Tag reportTag = sorted.get(i);
            String tagXPath = sorted.get(i).getFullTagXPath();
            Tag itr = sorted.get(i);
            while (tagXPath.contains(itr.getFullTagXPath())) {
                entryValue.add(itr);
                if ((i + 1) != sorted.size()) {
                    itr = sorted.get(++i);
                } else {
                    break;
                }
            }
            res.add(new Page.PageArea(reportTag, entryValue));
        } while (i + 1 < sorted.size());
        return res;
    }

    /**
     * Grouping tags from all pages with the same text content
     * return list of pages
     * 1 page has a lot of compoundtags
     */

    public static List<Page.PageArea> compactGroupBasedOnTextContent(List<Tag> theSameTextTags) {
        return groupByPageUrl(theSameTextTags)
                .values()
                .stream()
                .map(TagSimilarityAnalyzerFlowUtil::groupByBlock)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static Map<String, List<Tag>> groupByPageUrl(List<Tag> tags) {
        return tags.stream()
                .collect(Collectors.groupingBy(Tag::getPageUrl));
    }


    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
