package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GermanPrepositionalVerb(String germanPrepositionalVerb, String translation,
                                      String germanDefinition, String germanExampleSentence,
                                      String caseAfterThePreposition, boolean isSeparable, boolean isColloquial) implements Word {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GermanPrepositionalVerb that = (GermanPrepositionalVerb) o;

        return Objects.equals(germanPrepositionalVerb, that.germanPrepositionalVerb);
    }

    @Override
    public int hashCode() {
        return germanPrepositionalVerb != null ? germanPrepositionalVerb.hashCode() : 0;
    }

    @Override
    public boolean isEmpty() {
        return germanPrepositionalVerb == null || germanPrepositionalVerb.isEmpty();
    }
}
