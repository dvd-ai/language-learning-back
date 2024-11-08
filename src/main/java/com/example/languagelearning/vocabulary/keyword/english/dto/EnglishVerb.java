package com.example.languagelearning.vocabulary.keyword.english.dto;

public record EnglishVerb(
        String englishWord,
        String wordTranslation,
        String presentPerfectForm,
        String pastSimpleForm,
        String englishDefinition,
        String preposition,
        String englishExampleSentence,
        boolean isColloquial
) {
}
