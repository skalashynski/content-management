package com.productiveedge.content_mgmt_automation.flow.util;

import com.productiveedge.content_mgmt_automation.entity.tag.Tag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TagSimilarityAnalyzerFlowUtil {

    private BiFunction<Tag, Tag, Boolean> contains = (Tag a, Tag b) -> a.getFullTagXPath().contains(b.getFullTagXPath())
            || b.getFullTagXPath().contains(a.getFullTagXPath());

    /**
     * For example tag has fullTagXpath like
     * //body//main//div[2]//div[3]//footer//div//div//div[1]//div[5]//div//div[2]//div[5]
     * And another one has fullTagXpath like
     * //body//main//div[2]//div[3]//footer//div//div//div[1]//div[5]//div//div[2]//div[5]//a
     * <p>
     * As result these tags will be compacted to tag, which will has two values (Set).
     */

    public static long calculateInsideLevelOfTagXPath(Tag tag, List<Tag> pageTags) {
        return pageTags.stream().filter(e -> e.getFullTagXPath().startsWith(tag.getFullTagXPath())).count();
    }

    public static Map<String, List<Tag>> compact(List<Tag> tags) {
        Map<String, List<Tag>> res = new HashMap<>();
        List<Tag> sorted = tags.stream().
                sorted((o1, o2) -> o2.getFullTagXPath().compareTo(o1.getFullTagXPath()))
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
            res.put(key, entryValue);
        } while (i + 1 < sorted.size());
        return res;
    }

    public static List<Tag> compactGroupBasedOnTextContent(List<Tag> tags) {
        return null;
        /*
        return tags.stream()
                /* .collect(Collectors.groupingBy(Tag::getPageUrl))//grouping by pageUrl
                 .values()
                 .stream()
                .map(e-> compact(e))
                .flatMap(List::stream)
                .collect(Collectors.toList());*/
    }

    public static String chooseKey(String key1, String key2) {
        if (key1.contains(key2)) {
            return key2;
        }
        if (key2.contains(key1)) {
            return key1;
        }
        return null;
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
