package com.example.languagelearning.vocabulary.english.entity;


import com.example.languagelearning.vocabulary.english.dto.EnglishVocabularyTopic;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Locale;

@Entity
public class EnglishVocabularyTopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private EnglishVocabularyTopic englishVocabularyTopic;

    private Locale translationLanguage;

    public EnglishVocabularyTopicEntity() {
    }

    public EnglishVocabularyTopicEntity(EnglishVocabularyTopic englishVocabularyTopic, Locale translationLanguage) {
        this.englishVocabularyTopic = englishVocabularyTopic;
        this.translationLanguage = translationLanguage;
    }

    public EnglishVocabularyTopicEntity(Long id, EnglishVocabularyTopic englishVocabularyTopic, Locale translationLanguage) {
        this.id = id;
        this.englishVocabularyTopic = englishVocabularyTopic;
        this.translationLanguage = translationLanguage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnglishVocabularyTopic getEnglishVocabularyTopic() {
        return englishVocabularyTopic;
    }

    public void setEnglishVocabularyTopic(EnglishVocabularyTopic englishVocabularyTopic) {
        this.englishVocabularyTopic = englishVocabularyTopic;
    }

    public Locale getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(Locale translationLanguage) {
        this.translationLanguage = translationLanguage;
    }
}
