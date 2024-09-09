package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.common.Word;

import java.util.Objects;

public record GermanAdjective(String germanWord, String wordTranslation,
                              String preposition, String case_, String germanExampleSentence,
                              String germanDefinition, boolean isColloquial) implements Word {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GermanAdjective that = (GermanAdjective) o;

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

