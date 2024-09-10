package com.example.languagelearning.vocabulary.keyword.english.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
public record EnglishIdiom(
        String englishIdiom, String englishDefinition,
        String englishExampleSentence, String idiomTranslation
) implements Word {
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

    @Override
    public boolean isEmpty() {
        return englishIdiom == null || englishIdiom.isEmpty();
    }
}
