package com.example.languagelearning.vocabulary.keyword.english.dto;

import java.util.Objects;

public record EnglishVerb(
        String englishWord,
        String wordTranslation,
        String presentPerfectForm,
        String pastSimpleForm,
        String englishDefinition,
        String preposition,
        String englishExampleSentence,
        boolean isColloquial
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnglishVerb verb = (EnglishVerb) o;

        return Objects.equals(englishWord, verb.englishWord);
    }

    @Override
    public int hashCode() {
        return englishWord != null ? englishWord.hashCode() : 0;
    }
}
