package com.example.languagelearning.vocabulary.keyword.english.repo;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Service
public class EnglishVocabularyTopicEntityService {

    private final EnglishVocabularyTopicEntityRepo englishVocabularyTopicEntityRepo;

    public EnglishVocabularyTopicEntityService(EnglishVocabularyTopicEntityRepo englishVocabularyTopicEntityRepo) {
        this.englishVocabularyTopicEntityRepo = englishVocabularyTopicEntityRepo;
    }

    public EnglishVocabularyTopicEntity addTopicEntity(EnglishVocabularyTopicEntity englishVocabularyTopicEntity) {
        return englishVocabularyTopicEntityRepo.save(englishVocabularyTopicEntity);
    }

    public List<EnglishVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(String keyword, Locale translationLanguage) {
        try {
            return englishVocabularyTopicEntityRepo.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);
        } catch (Exception e) {
            throw new ApplicationException(e.getCause() + "\n" + e.getMessage());
        }
    }

}
