package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;

public class GermanVocabularyMapper {

    public GermanVocabularyMapper() {
    }

    public GermanVocabularyTopic mapToDto(GermanVocabularyTopicEntity entity) {
        GermanVocabularyTopic vocabularyTopic = entity.getGermanVocabularyTopic();

        return new GermanVocabularyTopic(
                vocabularyTopic.getVocabularyName(),
                vocabularyTopic.getVerbs(),
                vocabularyTopic.getNouns(),
                vocabularyTopic.getAdjectives(),
                vocabularyTopic.getCollocations(),
                vocabularyTopic.getIdioms(),
                vocabularyTopic.getPrepositionalVerbs()

        );
    }
}
