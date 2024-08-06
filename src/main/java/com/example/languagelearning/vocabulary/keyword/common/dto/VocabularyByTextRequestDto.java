package com.example.languagelearning.vocabulary.keyword.common.dto;


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
