package com.example.languagelearning.util;

import java.util.Map;
import java.util.TreeMap;

public class TextUtil {
    public static Map<Integer, String> breakTextIntoSortedParagraphs(String text) {
        text = text.replace("\r\n", "\n").replace("\r", "\n");

        String[] paragraphs = text.split("\\n\\n");

        Map<Integer, String> paragraphMap = new TreeMap<>();
        for (int i = 0; i < paragraphs.length; i++) {
            String paragraph = paragraphs[i].trim();
            if (!paragraph.isEmpty()) {
                paragraphMap.put(i + 1, paragraph);
            }
        }

        return paragraphMap;
    }
}
