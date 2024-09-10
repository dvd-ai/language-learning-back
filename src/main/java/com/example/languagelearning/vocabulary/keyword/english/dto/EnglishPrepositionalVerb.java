package com.example.languagelearning.vocabulary.keyword.english.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnglishPrepositionalVerb(
        String englishPrepositionalVerb, String translation,
        String englishDefinition, String englishExampleSentence, boolean isColloquial
) implements Word {
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

    @Override
    public boolean isEmpty() {
        return englishPrepositionalVerb == null || englishPrepositionalVerb.isEmpty();
    }
}
