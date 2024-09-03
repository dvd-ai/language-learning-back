package com.example.languagelearning.vocabulary.by_text.common;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.error.ClientException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@Service
public class VocabularyByTextManager {

    private final OpenAiService openAiService;

    private final Map<String, VocabularyByTextService> vocabularyServices;


    public VocabularyByTextManager(OpenAiService openAiService, ApplicationContext applicationContext) {
        this.openAiService = openAiService;
        this.vocabularyServices = applicationContext.getBeansOfType(VocabularyByTextService.class);
    }

    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto) {
        try {
            for (VocabularyByTextService vocabularyByTextService : vocabularyServices.values()) {
                if (vocabularyByTextService.getLanguage().equals(normalizeLocale(requestDto.targetLanguage())))
                    return vocabularyByTextService.getVocabularyByText(requestDto, openAiService);
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException("The vocabulary-by-text service for the language: '" + requestDto.targetLanguage() + "'  wasn't found");
    }
}
