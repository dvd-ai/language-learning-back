package com.example.languagelearning.vocabulary.common;

import java.util.List;

public class WordUtils {

    private WordUtils() {
    }
    public static void removeEmptyWordsPhrases(List<? extends Word> wordsPhrases) {
        wordsPhrases.removeIf(Word::isEmpty);
    }
}
