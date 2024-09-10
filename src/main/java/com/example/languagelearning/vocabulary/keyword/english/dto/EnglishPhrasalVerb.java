package com.example.languagelearning.vocabulary.keyword.english.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnglishPhrasalVerb(
        String englishPhrasalVerb,
        String translation,
        String englishDefinition,
        String preposition,
        String englishExampleSentence
) implements Word {
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

    @Override
    public boolean isEmpty() {
        return englishPhrasalVerb == null || englishPhrasalVerb.isEmpty();
    }
}
