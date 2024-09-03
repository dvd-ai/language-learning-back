package com.example.languagelearning.vocabulary.common.german;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicComparator;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@Component
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

    public GermanVocabularyTopicEntity mapToEntity(GermanVocabularyTopic vocabularyTopic, Locale translationLanguage) {
        return new GermanVocabularyTopicEntity(
                new GermanVocabularyTopic(
                        vocabularyTopic.getVocabularyName(),
                        vocabularyTopic.getVerbs(),
                        vocabularyTopic.getNouns(),
                        vocabularyTopic.getAdjectives(),
                        vocabularyTopic.getCollocations(),
                        vocabularyTopic.getIdioms(),
                        vocabularyTopic.getPrepositionalVerbs()
                ),
                normalizeLocale(translationLanguage)
        );
    }

    //temporal method
    public GermanVocabularyTopicEntity mapToEntity(GermanVocabularyTopic vocabularyTopic, String translationLanguage) {
        return new GermanVocabularyTopicEntity(
                new GermanVocabularyTopic(
                        vocabularyTopic.getVocabularyName(),
                        vocabularyTopic.getVerbs(),
                        vocabularyTopic.getNouns(),
                        vocabularyTopic.getAdjectives(),
                        vocabularyTopic.getCollocations(),
                        vocabularyTopic.getIdioms(),
                        vocabularyTopic.getPrepositionalVerbs()
                ),
                translationLanguage
        );
    }

    public List<GermanVocabularyTopicEntity> mapToEntities(List<GermanVocabularyTopic> vocabularyTopics, Locale translationLanguage) {
        return vocabularyTopics.stream()
                .map(topic -> mapToEntity(topic, translationLanguage))
                .toList();
    }

    public List<GermanVocabularyTopicEntity> mapToEntities(List<GermanVocabularyTopic> vocabularyTopics, String translationLanguage) {
        return vocabularyTopics.stream()
                .map(topic -> mapToEntity(topic, translationLanguage))
                .toList();
    }

    public List<GermanVocabularyTopicDto> mapToDtos(List<GermanVocabularyTopicEntity> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .sorted(new VocabularyTopicComparator())
                .toList();
    }
}
