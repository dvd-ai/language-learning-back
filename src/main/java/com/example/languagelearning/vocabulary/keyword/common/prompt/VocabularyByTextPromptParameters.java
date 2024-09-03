package com.example.languagelearning.vocabulary.keyword.common.prompt;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyByTextRequestDto;

public record VocabularyByTextPromptParameters(
        VocabularyByTextRequestDto requestDto, String text, Integer textNumber
) {
}
