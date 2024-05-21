package com.example.languagelearning.vocabulary.keyword.common;

import com.example.languagelearning.common.Language;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Locale;

public interface VocabularyKeywordService extends Language {
    List<? extends VocabularyTopic> processByKeyword(String keyword, OpenAiService openAiService, Locale translationLanguage) throws JsonProcessingException;
}
