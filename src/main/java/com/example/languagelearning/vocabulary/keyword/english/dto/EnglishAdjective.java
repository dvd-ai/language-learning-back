package com.example.languagelearning.vocabulary.keyword.english.dto;


import java.util.Objects;

public record EnglishAdjective(String englishWord, String wordTranslation,
                               String preposition, String englishExampleSentence,
                               String englishDefinition, boolean isColloquial) {

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
}

