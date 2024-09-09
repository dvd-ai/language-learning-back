package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.common.Word;

import java.util.Objects;

public record GermanIdiom(String germanIdiom, String germanDefinition,
                          String germanExampleSentence, String idiomTranslation) implements Word {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GermanIdiom that = (GermanIdiom) o;

        return Objects.equals(germanIdiom, that.germanIdiom);
    }

    @Override
    public int hashCode() {
        return germanIdiom != null ? germanIdiom.hashCode() : 0;
    }

    @Override
    public boolean isEmpty() {
        return germanIdiom == null || germanIdiom.isEmpty();
    }
}
