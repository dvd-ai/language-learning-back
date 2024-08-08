package com.example.languagelearning.vocabulary.keyword.english.dto;

import java.util.Objects;

public record EnglishPrepositionalVerb(
        String englishPrepositionalVerb, String translation,
        String englishDefinition, String englishExampleSentence, boolean isColloquial
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnglishPrepositionalVerb that = (EnglishPrepositionalVerb) o;

        return Objects.equals(englishPrepositionalVerb, that.englishPrepositionalVerb);
    }

    @Override
    public int hashCode() {
        return englishPrepositionalVerb != null ? englishPrepositionalVerb.hashCode() : 0;
    }
}
