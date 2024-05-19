package com.example.languagelearning.vocabulary.english.repo;

import com.example.languagelearning.vocabulary.english.entity.EnglishVocabularyTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Locale;

public interface EnglishVocabularyTopicEntityRepo extends JpaRepository<EnglishVocabularyTopicEntity, Long> {
    @Query(value = """
            SELECT id, translation_language, english_vocabulary_topic FROM english_vocabulary_topic_entity
                WHERE
                   translation_language = :translationLanguage AND
                   COALESCE(split_part(english_vocabulary_topic ->> 'vocabularyName', '.', 1), NULL) = :keyword
            """, nativeQuery = true)
    List<EnglishVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(@Param("keyword") String keyword,
                                                                                 @Param("translationLanguage") Locale translationLanguage
    );
}
