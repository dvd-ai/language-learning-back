package com.example.languagelearning.vocabulary.keyword.english.dto;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;

import java.util.Locale;

public class EnglishVocabularyTopicDto extends VocabularyTopicDto {

    private Locale targetLanguage;

    private Locale translationLanguage;
    private Long id;
    private EnglishVocabularyTopic englishVocabularyTopic;

    public EnglishVocabularyTopicDto(Locale targetLanguage, Locale translationLanguage,
                                     Long id, EnglishVocabularyTopic englishVocabularyTopic) {
        super(targetLanguage);
        this.targetLanguage = targetLanguage;
        this.translationLanguage = translationLanguage;
        this.id = id;
        this.englishVocabularyTopic = englishVocabularyTopic;
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

    public EnglishVocabularyTopic getEnglishVocabularyTopic() {
        return englishVocabularyTopic;
    }

    public void setEnglishVocabularyTopic(EnglishVocabularyTopic englishVocabularyTopic) {
        this.englishVocabularyTopic = englishVocabularyTopic;
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
