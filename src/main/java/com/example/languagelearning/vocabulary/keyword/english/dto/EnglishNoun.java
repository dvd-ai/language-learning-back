package com.example.languagelearning.vocabulary.keyword.english.dto;

public record EnglishNoun(
        String englishWord, String wordTranslation, String preposition,
        String englishExampleSentence, String englishDefinition, boolean isColloquial
) {
}
