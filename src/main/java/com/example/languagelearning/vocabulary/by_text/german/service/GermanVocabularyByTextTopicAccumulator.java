package com.example.languagelearning.vocabulary.by_text.german.service;

import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;

import java.util.ArrayList;
import java.util.List;
import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getParagraphedVocabularyName;

public final class GermanVocabularyByTextTopicAccumulator {

    private GermanVocabularyByTextTopicAccumulator() {
    }
    public static GermanVocabularyTopic getAccumulatedVocabularyTopic(List<GermanVocabularyTopic> vocabularyTopicParts, VocabularyByTextPromptParameters promptParameters) {
        GermanVocabularyTopic resultTopic = new GermanVocabularyTopic(
                getParagraphedVocabularyName(promptParameters),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>()
        );

        for (GermanVocabularyTopic vt : vocabularyTopicParts) {
            resultTopic.getVerbs().addAll(vt.getVerbs());
            resultTopic.getNouns().addAll(vt.getNouns());
            resultTopic.getAdjectives().addAll(vt.getAdjectives());
            resultTopic.getCollocations().addAll(vt.getCollocations());
            resultTopic.getIdioms().addAll(vt.getIdioms());
            resultTopic.getPrepositionalVerbs().addAll(vt.getPrepositionalVerbs());
        }

        return resultTopic;
    }
}
