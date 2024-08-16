package com.example.languagelearning.vocabulary.keyword.common.util;

import java.util.Iterator;
import java.util.List;

public class DuplicationUtil {

    private DuplicationUtil() {
    }

    public static <T> boolean isDuplicated(List<T> list, T item) {
        return list
                .stream()
                .filter(i -> i.equals(item))
                .count() > 1;
    }

    public static <T> void removeDuplicates(List<T> items) {
        Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (isDuplicated(items, item)) {
                iterator.remove();
            }
        }
    }

    public static <T> void removeDuplicatesBetweenLists(List<T> uniqueItems, List<T> duplicatedItems) {
        Iterator<T> iterator = duplicatedItems.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (uniqueItems.contains(item)) {
                iterator.remove();
            }
        }
    }

}
