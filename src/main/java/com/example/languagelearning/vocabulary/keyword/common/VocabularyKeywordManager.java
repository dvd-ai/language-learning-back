package com.example.languagelearning.vocabulary.keyword.common;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.error.ClientException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.languagelearning.common.LanguageUtil.equalLanguages;

@Service
public class VocabularyKeywordManager {

    private final OpenAiService openAiService;

    private final Map<String, VocabularyKeywordService> vocabularyServices;

    public VocabularyKeywordManager(OpenAiService openAiService, ApplicationContext applicationContext) {
        this.openAiService = openAiService;
        this.vocabularyServices = applicationContext.getBeansOfType(VocabularyKeywordService.class);
    }

    public List<? extends VocabularyTopic> getSeveralVocabularyByKeyword(String keyword, Locale targetLanguage, Locale translationLanguage) {
        try {
            for (VocabularyKeywordService vocabularyKeywordService : vocabularyServices.values()) {
                if (equalLanguages(vocabularyKeywordService.getLanguage(), targetLanguage))
                    return vocabularyKeywordService.processByKeyword(keyword, openAiService, translationLanguage);
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException("The service for the language: '" + targetLanguage + "'  wasn't found");
    }


}
