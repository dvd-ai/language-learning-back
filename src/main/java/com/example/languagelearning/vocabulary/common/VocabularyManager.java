package com.example.languagelearning.vocabulary.common;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.error.ClientException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.dto.VocabularyTopic;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class VocabularyManager {

    private final OpenAiService openAiService;

    private final Map<String, VocabularyService> vocabularyServices;

    public VocabularyManager(OpenAiService openAiService, ApplicationContext applicationContext) {
        this.openAiService = openAiService;
        this.vocabularyServices = applicationContext.getBeansOfType(VocabularyService.class);
    }

    public List<VocabularyTopic> getSeveralVocabularyByKeyword(String keyword, Locale targetLanguage, Locale translationLanguage) {
        try {
            for (VocabularyService vocabularyService: vocabularyServices.values()) {
                if (targetLanguage.equals(vocabularyService.getVocabularyLanguage()))
                    return vocabularyService.processByKeyword(keyword, openAiService, translationLanguage);
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException("The service for the language: '" + targetLanguage + "'  wasn't found");
    }
}
