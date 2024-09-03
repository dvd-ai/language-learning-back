package com.example.languagelearning.vocabulary.keyword.common.dto;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VocabularyTopicComparator implements Comparator<VocabularyTopicDto> {
    private static final Pattern PATTERN = Pattern.compile("(.*\\.P)(\\d+)$");

    @Override
    public int compare(VocabularyTopicDto topic1, VocabularyTopicDto topic2) {
        String name1 = topic1.getVocabularyName();
        String name2 = topic2.getVocabularyName();

        int baseComparison = compareBaseNames(name1, name2);
        if (baseComparison != 0) {
            return baseComparison;
        }

        return compareByLastNumericPart(name1, name2);
    }

    private int compareBaseNames(String name1, String name2) {
        int lastPIndex1 = name1.lastIndexOf(".P");
        int lastPIndex2 = name2.lastIndexOf(".P");

        if (lastPIndex1 != -1 && lastPIndex2 != -1) {
            String base1 = name1.substring(0, lastPIndex1);
            String base2 = name2.substring(0, lastPIndex2);
            return base1.compareToIgnoreCase(base2);
        }

        return name1.compareToIgnoreCase(name2);
    }

    private int compareByLastNumericPart(String name1, String name2) {
        Matcher matcher1 = PATTERN.matcher(name1);
        Matcher matcher2 = PATTERN.matcher(name2);

        if (matcher1.find() && matcher2.find()) {
            String numPart1 = matcher1.group(2);
            String numPart2 = matcher2.group(2);
            int num1 = Integer.parseInt(numPart1);
            int num2 = Integer.parseInt(numPart2);
            return Integer.compare(num1, num2);
        }

        return 0;
    }
}