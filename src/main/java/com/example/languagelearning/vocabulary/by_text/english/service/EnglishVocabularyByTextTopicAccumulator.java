package com.example.languagelearning.vocabulary.by_text.english.service;

import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;

import java.util.ArrayList;
import java.util.List;

import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getParagraphedVocabularyName;


public final class EnglishVocabularyByTextTopicAccumulator {

    private EnglishVocabularyByTextTopicAccumulator(){}

    public static EnglishVocabularyTopic getAccumulatedVocabularyTopic(List<EnglishVocabularyTopic> vocabularyTopicParts, VocabularyByTextPromptParameters promptParameters) {
        EnglishVocabularyTopic resultTopic = new EnglishVocabularyTopic(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                getParagraphedVocabularyName(promptParameters)
        );

        for (EnglishVocabularyTopic vt : vocabularyTopicParts) {
            resultTopic.getVerbs().addAll(vt.getVerbs());
            resultTopic.getNouns().addAll(vt.getNouns());
            resultTopic.getAdjectives().addAll(vt.getAdjectives());
            resultTopic.getCollocations().addAll(vt.getCollocations());
            resultTopic.getIdioms().addAll(vt.getIdioms());
            resultTopic.getPrepositionalVerbs().addAll(vt.getPrepositionalVerbs());
            resultTopic.getPhrasalVerbs().addAll(vt.getPhrasalVerbs());
        }
        return resultTopic;
    }
}
