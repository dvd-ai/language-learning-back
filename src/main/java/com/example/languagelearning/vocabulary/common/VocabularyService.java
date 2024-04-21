package com.example.languagelearning.vocabulary.common;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.dto.VocabularyTopic;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Locale;

public interface VocabularyService {
    Locale getVocabularyLanguage();

    List<VocabularyTopic> processByKeyword(String keyword, OpenAiService openAiService, Locale translationLanguage) throws JsonProcessingException;
}
