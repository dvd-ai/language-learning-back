package com.example.languagelearning.vocabulary.keyword.english;

import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;

import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

public class EnglishVocabularyMapper {

    public EnglishVocabularyMapper() {
    }

    public EnglishVocabularyTopicDto mapToDto(EnglishVocabularyTopicEntity entity) {
        EnglishVocabularyTopic vocabularyTopic = entity.getEnglishVocabularyTopic();

        return new EnglishVocabularyTopicDto(
                new Locale("en"),
                new Locale(entity.getTranslationLanguage()),
                entity.getId(),
                new EnglishVocabularyTopic(
                        vocabularyTopic.getVerbs(),
                        vocabularyTopic.getNouns(),
                        vocabularyTopic.getAdjectives(),
                        vocabularyTopic.getCollocations(),
                        vocabularyTopic.getIdioms(),
                        vocabularyTopic.getPrepositionalVerbs(),
                        vocabularyTopic.getPhrasalVerbs(),
                        vocabularyTopic.getVocabularyName()
                )
        );
    }

    public EnglishVocabularyTopicEntity mapToEntity(EnglishVocabularyTopicDto dto) {
        EnglishVocabularyTopic vocabularyTopic = dto.getEnglishVocabularyTopic();

        return new EnglishVocabularyTopicEntity(
                dto.getId(),
                new EnglishVocabularyTopic(
                        vocabularyTopic.getVerbs(),
                        vocabularyTopic.getNouns(),
                        vocabularyTopic.getAdjectives(),
                        vocabularyTopic.getCollocations(),
                        vocabularyTopic.getIdioms(),
                        vocabularyTopic.getPrepositionalVerbs(),
                        vocabularyTopic.getPhrasalVerbs(),
                        vocabularyTopic.getVocabularyName()
                ),
                normalizeLocale(dto.getTranslationLanguage())
        );
    }
}
