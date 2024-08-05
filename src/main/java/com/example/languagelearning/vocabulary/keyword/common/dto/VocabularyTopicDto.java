package com.example.languagelearning.vocabulary.keyword.common.dto;

import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopicDto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Locale;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnglishVocabularyTopicDto.class, name = "en"),
        @JsonSubTypes.Type(value = GermanVocabularyTopicDto.class, name = "de")
})
public abstract class VocabularyTopicDto {

    private final Locale type;

    public VocabularyTopicDto(Locale type) {
        this.type = getTargetLanguage();
    }

    public Locale getType() {
        return getTargetLanguage();
    }

    public abstract Locale getTargetLanguage();

    public abstract Locale getTranslationLanguage();
    public abstract void setTranslationLanguage(Locale translationLanguage);

    public abstract void setTargetLanguage(Locale targetLanguage);

    public abstract Long getId();
}
