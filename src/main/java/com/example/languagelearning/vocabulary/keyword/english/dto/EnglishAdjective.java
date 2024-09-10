package com.example.languagelearning.vocabulary.keyword.english.dto;


import com.example.languagelearning.vocabulary.common.Word;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnglishAdjective(String englishWord, String wordTranslation,
                               String preposition, String englishExampleSentence,
                               String englishDefinition, boolean isColloquial) implements Word {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnglishAdjective that = (EnglishAdjective) o;

        return Objects.equals(englishWord, that.englishWord);
    }

    @Override
    public int hashCode() {
        return englishWord != null ? englishWord.hashCode() : 0;
    }

    @Override
    public boolean isEmpty() {
        return englishWord == null || englishWord.isEmpty();
    }
}

