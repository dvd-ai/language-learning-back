package com.example.languagelearning.util;

import java.util.*;

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

    public static List<String> breakTextIntoSentencesParts(String text) {
        List<String> sentences = Arrays.stream(text.split("\\.")).toList();
        List<String> parts = new ArrayList<>();
        StringBuilder part = new StringBuilder();

        for (String sentence : sentences) {
            if (part.length() + sentence.length() < 900) {
                part.append(sentence).append(".");
            } else {
                parts.add(part.toString());
                part = new StringBuilder(sentence);
            }
        }
        if (!part.isEmpty()) {
            parts.add(part.toString());
        }
        return parts;
    }
}
