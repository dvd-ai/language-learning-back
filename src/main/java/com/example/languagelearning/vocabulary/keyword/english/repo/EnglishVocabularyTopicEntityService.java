package com.example.languagelearning.vocabulary.keyword.english.repo;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EnglishVocabularyTopicEntityService {

    private final EnglishVocabularyTopicEntityRepo englishVocabularyTopicEntityRepo;
    private final ObjectMapper objectMapper;

    public EnglishVocabularyTopicEntityService(EnglishVocabularyTopicEntityRepo englishVocabularyTopicEntityRepo, ObjectMapper objectMapper) {
        this.englishVocabularyTopicEntityRepo = englishVocabularyTopicEntityRepo;
        this.objectMapper = objectMapper;
    }

    public EnglishVocabularyTopicEntity addTopicEntity(EnglishVocabularyTopicEntity englishVocabularyTopicEntity) {
        return englishVocabularyTopicEntityRepo.save(englishVocabularyTopicEntity);
    }

    public List<EnglishVocabularyTopicEntity> addTopicEntities(List<EnglishVocabularyTopicEntity> englishVocabularyTopicEntities) {
        return englishVocabularyTopicEntityRepo.saveAll(englishVocabularyTopicEntities);
    }

    public List<EnglishVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(String keyword, String translationLanguage) {
        try {
            return englishVocabularyTopicEntityRepo.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);
        } catch (Exception e) {
            throw new ApplicationException(e.getCause() + "\n" + e.getMessage());
        }
    }

    public void updateVocabularyTopicEntity(EnglishVocabularyTopicEntity englishVocabularyTopicEntity) {
        try {
            String topicJson = objectMapper.writeValueAsString(englishVocabularyTopicEntity.getEnglishVocabularyTopic());
            englishVocabularyTopicEntityRepo.updateTopic(englishVocabularyTopicEntity.getId(), topicJson);
        } catch (Exception e) {
            throw new ApplicationException(e.getCause() + "\n" + e.getMessage());
        }
    }
}
