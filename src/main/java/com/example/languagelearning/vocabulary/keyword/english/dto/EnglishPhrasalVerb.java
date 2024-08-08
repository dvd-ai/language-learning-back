package com.example.languagelearning.vocabulary.keyword.english.dto;

import java.util.Objects;

public record EnglishPhrasalVerb(
        String englishPhrasalVerb,
        String translation,
        String englishDefinition,
        String preposition,
        String englishExampleSentence
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnglishPhrasalVerb that = (EnglishPhrasalVerb) o;

        return Objects.equals(englishPhrasalVerb, that.englishPhrasalVerb);
    }

    @Override
    public int hashCode() {
        return englishPhrasalVerb != null ? englishPhrasalVerb.hashCode() : 0;
    }
}
