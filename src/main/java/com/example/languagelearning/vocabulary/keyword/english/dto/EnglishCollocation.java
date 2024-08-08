package com.example.languagelearning.vocabulary.keyword.english.dto;

import java.util.Objects;

public record EnglishCollocation(
        String englishCollocation, String collocationTranslation,
        String englishDefinition, String englishExampleSentence
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnglishCollocation that = (EnglishCollocation) o;

        return Objects.equals(englishCollocation, that.englishCollocation);
    }

    @Override
    public int hashCode() {
        return englishCollocation != null ? englishCollocation.hashCode() : 0;
    }
}
