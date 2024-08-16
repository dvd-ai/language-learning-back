package com.example.languagelearning.vocabulary.keyword.german.dto;

import java.util.Objects;

public record GermanVerb(String germanWord, String wordTranslation,
                         String case_, String germanExampleSentence,
                         String pastTenseForm, String perfectTenseForm,
                         String germanDefinition, boolean isSeparable, boolean isColloquial) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GermanVerb that = (GermanVerb) o;

        return Objects.equals(germanWord, that.germanWord);
    }

    @Override
    public int hashCode() {
        return germanWord != null ? germanWord.hashCode() : 0;
    }
}
