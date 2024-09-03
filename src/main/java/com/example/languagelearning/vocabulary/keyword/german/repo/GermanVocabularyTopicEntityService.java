package com.example.languagelearning.vocabulary.keyword.german.repo;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GermanVocabularyTopicEntityService {

    private final GermanVocabularyTopicEntityRepo germanVocabularyTopicEntityRepo;
    private final ObjectMapper objectMapper;

    public GermanVocabularyTopicEntityService(GermanVocabularyTopicEntityRepo germanVocabularyTopicEntityRepo, ObjectMapper objectMapper) {
        this.germanVocabularyTopicEntityRepo = germanVocabularyTopicEntityRepo;
        this.objectMapper = objectMapper;
    }

    public GermanVocabularyTopicEntity addTopicEntity(GermanVocabularyTopicEntity germanVocabularyTopicEntity) {
        return germanVocabularyTopicEntityRepo.save(germanVocabularyTopicEntity);
    }

    public List<GermanVocabularyTopicEntity> addTopicEntities(List<GermanVocabularyTopicEntity> germanVocabularyTopicEntities) {
        return germanVocabularyTopicEntityRepo.saveAll(germanVocabularyTopicEntities);
    }

    public List<GermanVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(String keyword, String translationLanguage) {
        try {
            return germanVocabularyTopicEntityRepo.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);
        } catch (Exception e) {
            throw new ApplicationException(e.getCause() + "\n" + e.getMessage());
        }
    }

    public void updateVocabularyTopicEntity(GermanVocabularyTopicEntity topicEntity) {
        try {
            String entityJson = objectMapper.writeValueAsString(topicEntity.getGermanVocabularyTopic());
            germanVocabularyTopicEntityRepo.updateTopic(topicEntity.getId(), entityJson);
        } catch (Exception e) {
            throw new ApplicationException(e.getCause() + "\n" + e.getMessage());
        }
    }
}
