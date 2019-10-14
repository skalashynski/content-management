package com.productiveedge.content_mgmt_automation.flow.util;

import com.productiveedge.content_mgmt_automation.entity.tag.CompoundTag;
import com.productiveedge.content_mgmt_automation.entity.tag.Tag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TagSimilarityAnalyzerFlowUtil {


    /**
     * For example tag has fullTagXpath like
     * //body//main//div[2]//div[3]//footer//div//div//div[1]//div[5]//div//div[2]//div[5]
     * And another one has fullTagXpath like
     * //body//main//div[2]//div[3]//footer//div//div//div[1]//div[5]//div//div[2]//div[5]//a
     * <p>
     * As result these tags will be compacted to map where key is least-length value, value - List of Tags.
     * @return Map<String, List < Tag>>
     */

    public static List<CompoundTag> compact(List<Tag> tags) {
        //Map<String, List<Tag>> res = new HashMap<>();
        List<CompoundTag> res = new ArrayList<>();
        List<Tag> sorted = tags.stream()
                .sorted((o1, o2) -> o2.getFullTagXPath().compareTo(o1.getFullTagXPath()))
                .collect(Collectors.toList());
        int i = 0;
        do {
            List<Tag> entryValue = new ArrayList<>();
            String key = sorted.get(i).getFullTagXPath();
            Tag itr = sorted.get(i);
            while (key.contains(itr.getFullTagXPath())) {
                entryValue.add(itr);
                if ((i + 1) != sorted.size()) {
                    itr = sorted.get(++i);
                } else {
                    break;
                }
            }
            res.add(new CompoundTag(key, entryValue));
            //res.put(key, entryValue);
        } while (i + 1 < sorted.size());
        return res;
    }

    /**
     * Grouping tags from all pages with the same text content
     * return list of pages
     * 1 page has a lot of compoundtags
     */

    public static List<CompoundTag> compactGroupBasedOnTextContent(List<Tag> tags) {
        return tags.stream()
                .collect(Collectors.groupingBy(Tag::getPageUrl))//grouping by pageUrl
                .values()
                 .stream()
                .map(e-> compact(e))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    /*
            return tags.stream().collect(Collectors.groupingBy(Tag::getTextContent))
                    .values()
                    .stream()
                    .map(list -> {
                        if (list.size() > 1) {
                            List<Tag> sorted = list.stream()
                                    .sorted(Comparator.comparingInt((Tag aTag) -> aTag.getFullTagXPath().length()))
                                    //.map(Tag::getFullTagXPath)
                                    .collect(Collectors.toList());
                            Tag minFullTanXpathLengthBaseTag = sorted.get(0);
                            Tag maxFullTanXpathLengthBaseTag = sorted.get(sorted.size() - 1);
                            return Arrays.asList(maxFullTanXpathLengthBaseTag);
                        }
                        return list;
                    })
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
    */
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
