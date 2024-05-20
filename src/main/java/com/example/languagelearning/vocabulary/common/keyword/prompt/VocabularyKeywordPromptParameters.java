package com.example.languagelearning.vocabulary.common.keyword.prompt;

import java.util.Locale;

public record VocabularyKeywordPromptParameters(String keyword, String subtopic0Level,
                                                String subtopic1Level, Locale translationLanguage) {
}
