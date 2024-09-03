package com.example.languagelearning.vocabulary.keyword.german.dto;

import java.util.Objects;

public record GermanIdiom(String germanIdiom, String germanDefinition,
                          String germanExampleSentence, String idiomTranslation) {
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
}
