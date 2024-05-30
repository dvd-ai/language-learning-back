package com.example.languagelearning.vocabulary.keyword.german.dto;

public record GermanVerb(String germanWord, String wordTranslation,
                         String preposition, String case_, String germanExampleSentence,
                         String pastTenseForm, String perfectTenseForm,
                         String germanDefinition, boolean isColloquial) {
}
