package com.example.languagelearning.vocabulary.keyword.common;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.error.ClientException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@Service
public class VocabularyKeywordManager {

    private final OpenAiService openAiService;

    private final Map<String, VocabularyKeywordService> vocabularyServices;

    public VocabularyKeywordManager(OpenAiService openAiService, ApplicationContext applicationContext) {
        this.openAiService = openAiService;
        this.vocabularyServices = applicationContext.getBeansOfType(VocabularyKeywordService.class);
    }

    public List<? extends VocabularyTopicDto> getSeveralVocabularyByKeyword(String keyword, String targetLanguage, String translationLanguage) {
        try {
            for (VocabularyKeywordService vocabularyKeywordService : vocabularyServices.values()) {
                if (vocabularyKeywordService.getLanguage().equals(targetLanguage))
                    return vocabularyKeywordService.processByKeyword(keyword, openAiService, translationLanguage);
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        throw new ClientException("The service for the language: '" + targetLanguage + "'  wasn't found");
    }

    @Transactional(rollbackFor = ApplicationException.class)
    public void updateTopics(List<? extends VocabularyTopicDto> vocabularyTopics) {

        try {
            for (VocabularyTopicDto vocabularyTopicDto : vocabularyTopics) {
                String currentLanguage = normalizeLocale(vocabularyTopicDto.getTargetLanguage());
                VocabularyKeywordService service = getVocabularyKeywordService(currentLanguage);
                if (service != null) {
                    service.updateVocabularyTopic(vocabularyTopicDto);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    private VocabularyKeywordService getVocabularyKeywordService(String language) {
        for (VocabularyKeywordService vocabularyKeywordService : vocabularyServices.values()) {
            if (vocabularyKeywordService.getLanguage().equals(language)) {
                return vocabularyKeywordService;
            }
        }
        return null;
    }
}
