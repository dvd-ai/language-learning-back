package com.example.languagelearning.vocabulary.keyword.english.repo;

import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnglishVocabularyTopicEntityRepo extends JpaRepository<EnglishVocabularyTopicEntity, Long> {
    @Query(value = """
            SELECT id, translation_language, english_vocabulary_topic FROM english_vocabulary_topic_entity
                WHERE
                   translation_language = :translationLanguage AND
                   COALESCE(split_part(english_vocabulary_topic ->> 'vocabularyName', '.', 1), NULL) = :keyword
            """, nativeQuery = true)
    List<EnglishVocabularyTopicEntity> findTopicsByKeywordAndTranslationLanguage(@Param("keyword") String keyword,
                                                                                 @Param("translationLanguage") String translationLanguage
    );

    @Modifying
    @Query(value = """
            UPDATE english_vocabulary_topic_entity
            SET english_vocabulary_topic = CAST(:updatedTopic AS JSONB)
            WHERE id = :id
            """, nativeQuery = true)
    int updateTopic(@Param("id") Long id, @Param("updatedTopic") String updatedTopic);
}
