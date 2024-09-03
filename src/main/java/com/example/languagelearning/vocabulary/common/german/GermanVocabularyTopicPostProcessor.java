package com.example.languagelearning.vocabulary.common.german;

import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;

import java.util.Iterator;
import java.util.List;

import static com.example.languagelearning.vocabulary.keyword.common.util.DuplicationUtil.removeDuplicates;
import static com.example.languagelearning.vocabulary.keyword.common.util.DuplicationUtil.removeDuplicatesBetweenLists;

public class GermanVocabularyTopicPostProcessor {

    private GermanVocabularyTopicPostProcessor() {
    }

    public static void performCleanup(List<GermanVocabularyTopic> vocabularyTopics) {
        vocabularyTopics.forEach(GermanVocabularyTopicPostProcessor::removeDuplicatedWordsInsideTopic);
        removeDuplicatesOutsideTheTopic(vocabularyTopics);
        removeEmptyTopics(vocabularyTopics);
    }

    private static void removeDuplicatesOutsideTheTopic(List<GermanVocabularyTopic> vocabularyTopics) {
        for (int i = 0; i < vocabularyTopics.size(); i++) {
            GermanVocabularyTopic topic1 = vocabularyTopics.get(i);
            for (int j = i + 1; j < vocabularyTopics.size(); j++) {
                GermanVocabularyTopic topic2 = vocabularyTopics.get(j);
                removeDuplicatesBetweenLists(topic1.getVerbs(), topic2.getVerbs());
                removeDuplicatesBetweenLists(topic1.getNouns(), topic2.getNouns());
                removeDuplicatesBetweenLists(topic1.getAdjectives(), topic2.getAdjectives());
                removeDuplicatesBetweenLists(topic1.getCollocations(), topic2.getCollocations());
                removeDuplicatesBetweenLists(topic1.getPrepositionalVerbs(), topic2.getPrepositionalVerbs());
                removeDuplicatesBetweenLists(topic1.getIdioms(), topic2.getIdioms());
            }
        }
    }

    private static void removeDuplicatedWordsInsideTopic(GermanVocabularyTopic vocabularyTopic) {
        removeDuplicates(vocabularyTopic.getVerbs());
        removeDuplicates(vocabularyTopic.getNouns());
        removeDuplicates(vocabularyTopic.getAdjectives());
        removeDuplicates(vocabularyTopic.getCollocations());
        removeDuplicates(vocabularyTopic.getPrepositionalVerbs());
        removeDuplicates(vocabularyTopic.getIdioms());
    }

    private static boolean isTopicEmpty(GermanVocabularyTopic vocabularyTopic) {
        return vocabularyTopic.getVerbs().isEmpty() && vocabularyTopic.getNouns().isEmpty()
                && vocabularyTopic.getAdjectives().isEmpty() && vocabularyTopic.getCollocations().isEmpty()
                && vocabularyTopic.getPrepositionalVerbs().isEmpty() && vocabularyTopic.getIdioms().isEmpty();
    }

    private static void removeEmptyTopics(List<GermanVocabularyTopic> vocabularyTopics) {
        Iterator<GermanVocabularyTopic> iterator = vocabularyTopics.iterator();
        while (iterator.hasNext()) {
            GermanVocabularyTopic vocabularyTopic = iterator.next();
            if (isTopicEmpty(vocabularyTopic)) {
                iterator.remove();
            }
        }
    }
}
