package com.example.languagelearning.vocabulary.keyword.common;

import com.example.languagelearning.common.Language;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface VocabularyKeywordService extends Language {
    List<? extends VocabularyTopicDto> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException;

    void updateVocabularyTopic(VocabularyTopicDto vocabularyTopicDto);
}
