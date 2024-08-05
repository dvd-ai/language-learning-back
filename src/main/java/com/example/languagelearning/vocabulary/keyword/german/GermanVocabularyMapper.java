package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;

import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

public class GermanVocabularyMapper {

    public GermanVocabularyMapper() {
    }

    public GermanVocabularyTopicDto mapToDto(GermanVocabularyTopicEntity entity) {
        GermanVocabularyTopic vocabularyTopic = entity.getGermanVocabularyTopic();

        return new GermanVocabularyTopicDto(
                new Locale("de"),
                new Locale(entity.getTranslationLanguage()),
                entity.getId(),
                new GermanVocabularyTopic(
                        vocabularyTopic.getVocabularyName(),
                        vocabularyTopic.getVerbs(),
                        vocabularyTopic.getNouns(),
                        vocabularyTopic.getAdjectives(),
                        vocabularyTopic.getCollocations(),
                        vocabularyTopic.getIdioms(),
                        vocabularyTopic.getPrepositionalVerbs()
                )
        );
    }

    public GermanVocabularyTopicEntity mapToEntity(GermanVocabularyTopicDto dto) {
        GermanVocabularyTopic vocabularyTopic = dto.getGermanVocabularyTopic();

        return new GermanVocabularyTopicEntity(
                dto.getId(),
                new GermanVocabularyTopic(
                        vocabularyTopic.getVocabularyName(),
                        vocabularyTopic.getVerbs(),
                        vocabularyTopic.getNouns(),
                        vocabularyTopic.getAdjectives(),
                        vocabularyTopic.getCollocations(),
                        vocabularyTopic.getIdioms(),
                        vocabularyTopic.getPrepositionalVerbs()
                ),
                normalizeLocale(dto.getTranslationLanguage())
        );
    }
}
