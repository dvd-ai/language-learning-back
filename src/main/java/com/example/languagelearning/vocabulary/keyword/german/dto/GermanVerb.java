package com.example.languagelearning.vocabulary.keyword.german.dto;

public record GermanVerb(String germanWord, String wordTranslation,
                         String case_, String germanExampleSentence,
                         String pastTenseForm, String perfectTenseForm,
                         String germanDefinition, boolean isSeparable, boolean isColloquial) {
}
