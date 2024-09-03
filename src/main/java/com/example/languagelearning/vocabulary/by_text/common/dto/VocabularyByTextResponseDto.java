package com.example.languagelearning.vocabulary.by_text.common.dto;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;

import java.util.List;
import java.util.Map;

public class VocabularyByTextResponseDto {
    VocabularyTopicDto vocabularyTopicDto;
    private Map<Integer, List<String>> wordsForParagraphs;
}