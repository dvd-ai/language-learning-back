package com.example.languagelearning.vocabulary.keyword.german.dto;

import java.util.Objects;

public record GermanNoun(String germanWord, String wordTranslation,
                         String germanExampleSentence, String pluralForm,
                         String germanDefinition, boolean isColloquial) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GermanNoun that = (GermanNoun) o;

        return Objects.equals(germanWord, that.germanWord);
    }

    @Override
    public int hashCode() {
        return germanWord != null ? germanWord.hashCode() : 0;
    }
}
