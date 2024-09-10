package com.example.languagelearning.vocabulary.by_text.common.dto;


import java.util.Locale;

public record VocabularyByTextRequestDto(
        Locale translationLanguage,
        Locale targetLanguage,
        String minLanguageLevel,
        String maxLanguageLevel,
        String textTopicLabel,

        String textContent
) {
}
