package com.example.languagelearning.vocabulary.keyword.english.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
public record EnglishCollocation(
        String englishCollocation, String collocationTranslation,
        String englishDefinition, String englishExampleSentence
) implements Word {
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

    @Override
    public boolean isEmpty() {
        return englishCollocation == null || englishCollocation.isEmpty();
    }
}
