package com.example.languagelearning.vocabulary.common.keyword;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.keyword.dto.VocabularyTopic;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Locale;

public interface VocabularyKeywordService {
    Locale getVocabularyLanguage();

    List<? extends VocabularyTopic> processByKeyword(String keyword, OpenAiService openAiService, Locale translationLanguage) throws JsonProcessingException;
}
