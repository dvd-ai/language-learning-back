package com.example.languagelearning.vocabulary.english.repo;

import com.example.languagelearning.vocabulary.english.entity.EnglishVocabularyTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnglishVocabularyRepo extends JpaRepository<EnglishVocabularyTopicEntity, Long> {
}
