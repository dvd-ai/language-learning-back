package com.example.languagelearning.vocabulary.keyword.german.repo;

import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GermanVocabularyTopicEntityRepo extends JpaRepository<GermanVocabularyTopicEntity, Long> {
    @Query(value = """
            SELECT id, translation_language, german_vocabulary_topic FROM german_vocabulary_topic_entity
                WHERE
                   translation_language = :translationLanguage AND
                   COALESCE(split_part(german_vocabulary_topic ->> 'vocabularyName', '.', 1), NULL) = :keyword
            """, nativeQuery = true)
    List<GermanVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(@Param("keyword") String keyword,
                                                                                @Param("translationLanguage") String translationLanguage
    );
}