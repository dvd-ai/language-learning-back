package com.example.languagelearning.vocabulary.crud.common;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.error.ClientException;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VocabularyCrudManager {
    private final Map<String, VocabularyCrudService> vocabularyCrudServices;



    public VocabularyCrudManager(ApplicationContext applicationContext) {
        this.vocabularyCrudServices = applicationContext.getBeansOfType(VocabularyCrudService.class);
    }

    public VocabularyTopicDto getVocabularyTopic(Long id, String targetLanguage) {
        try {
            for (VocabularyCrudService vocabularyCrudService : vocabularyCrudServices.values()) {
                if (vocabularyCrudService.getLanguage().equals(targetLanguage)) {
                    return vocabularyCrudService.getVocabularyTopic(id, targetLanguage);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException(getErrorMessageNotFoundService(targetLanguage));
    }

    public Page<VocabularyTopicDto> getAllVocabularyTopics(String targetLanguage, Pageable pageable) {
        try {
            for (VocabularyCrudService vocabularyCrudService : vocabularyCrudServices.values()) {
                if (vocabularyCrudService.getLanguage().equals(targetLanguage)) {
                    return vocabularyCrudService.getAllVocabularyTopics(pageable);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException(getErrorMessageNotFoundService(targetLanguage));

    }

    public Page<VocabularyTopicDto>getVocabularyTopicsBySearch(String targetLanguage, String search, Pageable pageable) {
        try {
            for (VocabularyCrudService vocabularyCrudService : vocabularyCrudServices.values()) {
                if (vocabularyCrudService.getLanguage().equals(targetLanguage)) {
                    return vocabularyCrudService.getVocabularyTopicsBySearch(search, pageable);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException(getErrorMessageNotFoundService(targetLanguage));
    }

    private String getErrorMessageNotFoundService(String targetLanguage) {
        return "The vocabulary-crud service for the language: '" + targetLanguage + "'  wasn't found";
    }
}
