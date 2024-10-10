package com.example.languagelearning.vocabulary.crud.german;

import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GermanVocabularyCrudRepo extends JpaRepository<GermanVocabularyTopicEntity, Long> {

    @Query(value = """
            SELECT id, translation_language, german_vocabulary_topic FROM german_vocabulary_topic_entity
                WHERE
                   german_vocabulary_topic ->> 'vocabularyName' LIKE %:search%
            """, nativeQuery = true,
            countQuery = """
        SELECT COUNT(*) translation_language, german_vocabulary_topic FROM german_vocabulary_topic_entity
        WHERE german_vocabulary_topic ->> 'vocabularyName' LIKE %:search%
        """
    )
    Page<GermanVocabularyTopicEntity> findVocabularyTopicsBySearch(@Param("search") String search, Pageable pageable);
}
