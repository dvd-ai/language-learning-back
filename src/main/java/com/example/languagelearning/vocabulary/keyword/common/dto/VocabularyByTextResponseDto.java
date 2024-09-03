package com.example.languagelearning.vocabulary.keyword.common.dto;

import java.util.List;
import java.util.Map;

public class VocabularyByTextResponseDto {
    VocabularyTopicDto vocabularyTopicDto;
    private Map<Integer, List<String>> wordsForParagraphs;
}
