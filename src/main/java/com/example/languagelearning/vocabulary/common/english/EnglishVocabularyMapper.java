package com.example.languagelearning.vocabulary.common.english;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicComparator;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@Component
public class EnglishVocabularyMapper {

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

    public EnglishVocabularyTopicEntity mapToEntity(EnglishVocabularyTopic vocabularyTopic, Locale translationLanguage) {
        return new EnglishVocabularyTopicEntity(
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
                normalizeLocale(translationLanguage)
        );
    }

    public List<EnglishVocabularyTopicEntity> mapToEntities(List<EnglishVocabularyTopic> vocabularyTopics, Locale translationLanguage) {
        return vocabularyTopics.stream()
                .map(topic -> mapToEntity(topic, translationLanguage))
                .toList();
    }

    public List<EnglishVocabularyTopicDto> mapToDtos(List<EnglishVocabularyTopicEntity> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .sorted(new VocabularyTopicComparator())
                .toList();
    }
}
