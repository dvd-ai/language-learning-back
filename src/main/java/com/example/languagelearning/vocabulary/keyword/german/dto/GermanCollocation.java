package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GermanCollocation(String germanCollocation, String collocationTranslation,
                                String germanDefinition, String germanExampleSentence) implements Word {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GermanCollocation that = (GermanCollocation) o;

        return Objects.equals(germanCollocation, that.germanCollocation);
    }

    @Override
    public int hashCode() {
        return germanCollocation != null ? germanCollocation.hashCode() : 0;
    }

    @Override
    public boolean isEmpty() {
        return germanCollocation == null || germanCollocation.isEmpty();
    }
}
