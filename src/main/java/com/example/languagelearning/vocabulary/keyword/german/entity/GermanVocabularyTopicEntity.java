package com.example.languagelearning.vocabulary.keyword.german.entity;

import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class GermanVocabularyTopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private GermanVocabularyTopic germanVocabularyTopic;

    private String translationLanguage;

    public GermanVocabularyTopicEntity() {
    }

    public GermanVocabularyTopicEntity(GermanVocabularyTopic germanVocabularyTopic, String translationLanguage) {
        this.germanVocabularyTopic = germanVocabularyTopic;
        this.translationLanguage = translationLanguage;
    }

    public GermanVocabularyTopicEntity(Long id, GermanVocabularyTopic germanVocabularyTopic, String translationLanguage) {
        this.id = id;
        this.germanVocabularyTopic = germanVocabularyTopic;
        this.translationLanguage = translationLanguage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GermanVocabularyTopic getGermanVocabularyTopic() {
        return germanVocabularyTopic;
    }

    public void setGermanVocabularyTopic(GermanVocabularyTopic englishVocabularyTopic) {
        this.germanVocabularyTopic = englishVocabularyTopic;
    }

    public String getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(String translationLanguage) {
        this.translationLanguage = translationLanguage;
    }
}
