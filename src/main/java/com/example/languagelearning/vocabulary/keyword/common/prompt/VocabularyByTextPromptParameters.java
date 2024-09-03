package com.example.languagelearning.vocabulary.keyword.common.prompt;

import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;

public record VocabularyByTextPromptParameters(
        VocabularyByTextRequestDto requestDto, String text, Integer textNumber
) {
}
