package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;

import java.util.Locale;

public class GermanVocabularyTopicDto extends VocabularyTopicDto {

    private Locale targetLanguage;
    private Locale translationLanguage;
    private Long id;
    private GermanVocabularyTopic germanVocabularyTopic;

    public GermanVocabularyTopicDto(Locale targetLanguage, Locale translationLanguage,
                                    Long id, GermanVocabularyTopic germanVocabularyTopic) {
        super(targetLanguage);
        this.targetLanguage = targetLanguage;
        this.translationLanguage = translationLanguage;
        this.id = id;
        this.germanVocabularyTopic = germanVocabularyTopic;
    }

    @Override
    public void setTargetLanguage(Locale targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    @Override
    public void setTranslationLanguage(Locale translationLanguage) {
        this.translationLanguage = translationLanguage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GermanVocabularyTopic getGermanVocabularyTopic() {
        return germanVocabularyTopic;
    }

    public void setGermanVocabularyTopic(GermanVocabularyTopic germanVocabularyTopic) {
        this.germanVocabularyTopic = germanVocabularyTopic;
    }

    @Override
    public Locale getTargetLanguage() {
        return this.targetLanguage;
    }

    @Override
    public Locale getTranslationLanguage() {
        return this.translationLanguage;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
