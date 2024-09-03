package com.example.languagelearning.vocabulary.keyword.english.dto;

import java.util.Objects;

public record EnglishIdiom(
        String englishIdiom, String englishDefinition,
        String englishExampleSentence, String idiomTranslation
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnglishIdiom that = (EnglishIdiom) o;

        return Objects.equals(englishIdiom, that.englishIdiom);
    }

    @Override
    public int hashCode() {
        return englishIdiom != null ? englishIdiom.hashCode() : 0;
    }
}
