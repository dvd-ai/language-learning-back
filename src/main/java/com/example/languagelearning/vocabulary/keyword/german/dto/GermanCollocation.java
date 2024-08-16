package com.example.languagelearning.vocabulary.keyword.german.dto;

import java.util.Objects;

public record GermanCollocation(String germanCollocation, String collocationTranslation,
                                String germanDefinition, String germanExampleSentence) {
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
}
