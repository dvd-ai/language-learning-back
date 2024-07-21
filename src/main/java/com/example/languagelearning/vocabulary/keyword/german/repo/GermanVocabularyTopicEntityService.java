package com.example.languagelearning.vocabulary.keyword.german.repo;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GermanVocabularyTopicEntityService {

    private final GermanVocabularyTopicEntityRepo germanVocabularyTopicEntityRepo;

    public GermanVocabularyTopicEntityService(GermanVocabularyTopicEntityRepo germanVocabularyTopicEntityRepo) {
        this.germanVocabularyTopicEntityRepo = germanVocabularyTopicEntityRepo;
    }

    public GermanVocabularyTopicEntity addTopicEntity(GermanVocabularyTopicEntity germanVocabularyTopicEntity) {
        return germanVocabularyTopicEntityRepo.save(germanVocabularyTopicEntity);
    }

    public List<GermanVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(String keyword, String translationLanguage) {
        try {
            return germanVocabularyTopicEntityRepo.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);
        } catch (Exception e) {
            throw new ApplicationException(e.getCause() + "\n" + e.getMessage());
        }
    }

}
