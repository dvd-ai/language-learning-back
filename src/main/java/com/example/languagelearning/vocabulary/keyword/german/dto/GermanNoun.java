package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GermanNoun(String germanWord, String wordTranslation,
                         String germanExampleSentence, String pluralForm,
                         String germanDefinition, boolean isColloquial) implements Word {
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

    @Override
    public boolean isEmpty() {
        return germanWord == null || germanWord.isEmpty();
    }
}
