package com.example.languagelearning.vocabulary.crud.english;

import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnglishVocabularyCrudRepo extends JpaRepository<EnglishVocabularyTopicEntity, Long> {

    @Query(value = """
            SELECT id, translation_language, english_vocabulary_topic FROM english_vocabulary_topic_entity
                WHERE
                   english_vocabulary_topic ->> 'vocabularyName' LIKE %:search%
            """, nativeQuery = true,
            countQuery = """
        SELECT COUNT(*) translation_language, english_vocabulary_topic FROM english_vocabulary_topic_entity
        WHERE english_vocabulary_topic ->> 'vocabularyName' LIKE %:search%
        """
    )
    Page<EnglishVocabularyTopicEntity> findVocabularyTopicsBySearch(@Param("search") String search, Pageable pageable);
}

