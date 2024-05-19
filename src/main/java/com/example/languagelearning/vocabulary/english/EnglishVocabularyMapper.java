package com.example.languagelearning.vocabulary.english;

import com.example.languagelearning.vocabulary.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.english.entity.EnglishVocabularyTopicEntity;

public class EnglishVocabularyMapper {

    public EnglishVocabularyMapper() {}

    public EnglishVocabularyTopic mapToDto(EnglishVocabularyTopicEntity entity) {
        EnglishVocabularyTopic vocabularyTopic = entity.getEnglishVocabularyTopic();

        return new EnglishVocabularyTopic(
                vocabularyTopic.getVerbs(),
                vocabularyTopic.getNouns(),
                vocabularyTopic.getAdjectives(),
                vocabularyTopic.getCollocations(),
                vocabularyTopic.getIdioms(),
                vocabularyTopic.getPrepositionalVerbs(),
                vocabularyTopic.getPhrasalVerbs(),
                vocabularyTopic.getVocabularyName()
        );
    }
}
