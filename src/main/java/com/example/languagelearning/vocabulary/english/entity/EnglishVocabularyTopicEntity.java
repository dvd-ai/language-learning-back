package com.example.languagelearning.vocabulary.english.entity;


import com.example.languagelearning.vocabulary.english.dto.EnglishVocabularyTopic;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class EnglishVocabularyTopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private EnglishVocabularyTopic englishVocabularyTopic;

    public EnglishVocabularyTopic getEnglishVocabularyTopic() {
        return englishVocabularyTopic;
    }

    public void setEnglishVocabularyTopic(EnglishVocabularyTopic englishVocabularyTopic) {
        this.englishVocabularyTopic = englishVocabularyTopic;
    }

    public EnglishVocabularyTopicEntity() {
    }

    public EnglishVocabularyTopicEntity(EnglishVocabularyTopic englishVocabularyTopic) {
        this.englishVocabularyTopic = englishVocabularyTopic;
    }

    public EnglishVocabularyTopicEntity(Long id, EnglishVocabularyTopic englishVocabularyTopic) {
        this.id = id;
        this.englishVocabularyTopic = englishVocabularyTopic;
    }
}
